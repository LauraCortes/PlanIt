package com.example.laura.planit.Activities.Contactos;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.PersitenciaService;

import java.util.List;

/**
 * Created by Laura on 18/09/2016.
 */
public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactoRowViewHolder>
{
    private Context context;
    List<Contacto> contactos;
    ContactRecyclerViewAdapter recycler;
    ContactosTabFragment contactosTabFragment;


    public ContactRecyclerViewAdapter(Context context, List<Contacto> contactos, ContactosTabFragment contactosTabFragment)
    {
        this.contactos=contactos;
        recycler=this;
        this.context=context;
        this.contactosTabFragment=contactosTabFragment;
    }

    @Override
    public ContactoRowViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_contacto, null);
        return  new ContactoRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactoRowViewHolder contactoRowViewHolder, final int position)
    {
        Contacto contacto = contactos.get(position);
        String nombre = contacto.getNombre();
        contactoRowViewHolder.textViewTitle.setText(nombre);
        contactoRowViewHolder.textViewSubtitle.setText(contacto.getNumeroTelefonico());
        Drawable iniciales =darImagenIniciales(nombre,position);
        contactoRowViewHolder.imgCirculo=iniciales;
        contactoRowViewHolder.circuloIniciales.setImageDrawable(iniciales);
        contactoRowViewHolder.decorarFavorito(contacto.isFavorito()==1);
        contactoRowViewHolder.decorarSeleccionado(contactosTabFragment.isItemSelected(position));
        final ContactoRowViewHolder finalContactoRowViewHolder = contactoRowViewHolder;

        //Marcar contacto como Favorito
        contactoRowViewHolder.favorito.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Contacto contactoFavorito = contactos.get(position);
                Intent intent = new Intent(context, PersitenciaService.class);
                intent.putExtra("Requerimiento","MarcarContacto");
                intent.putExtra("Contacto",PlanIt.darInstancia().darContacto(position));
                if(contactoFavorito.isFavorito()==0)
                {
                    finalContactoRowViewHolder.decorarFavorito(true);
                    contactoFavorito.setFavorito(1);
                    intent.putExtra("Favoritos",1);

                }
                else
                {
                    finalContactoRowViewHolder.decorarFavorito(false);
                    contactoFavorito.setFavorito(0);
                    intent.putExtra("Favoritos",0);
                }
                context.startService(intent);
            }
        });

        contactoRowViewHolder.vista.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(contactosTabFragment.modoEliminar())
                {
                    seleccionarItem(position,contactoRowViewHolder);
                }
            }
        });

        contactoRowViewHolder.vista.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                seleccionarItem(position,contactoRowViewHolder);
                //Retorna que sí lo usó->No invoca el clickListener
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (contactos == null) {
            return 0;
        } else {
            return contactos.size();
        }
    }

    public Drawable darImagenIniciales(String texto, int pos)
    {
        String[] separaciones = texto.trim().split(" ");
        String iniciales="";
        for(int i=0; i<separaciones.length && i<2;i++)
        {
            if(separaciones[i].trim().length()>0)
            {
                iniciales+=separaciones[i].trim().charAt(0);
            }
        }
        if(iniciales.isEmpty())
        {
            iniciales="-";
        }
        return TextDrawable.builder().buildRound(iniciales.toUpperCase(), ContactoRowViewHolder.COLORES[pos%5]);

    }

    private void seleccionarItem(int position, ContactoRowViewHolder contactoRowViewHolder)
    {
        boolean seleccionado = contactosTabFragment.isItemSelected(position);
        contactoRowViewHolder.decorarSeleccionado(!seleccionado);
        contactosTabFragment.seleccionarItem(position);
    }
}
