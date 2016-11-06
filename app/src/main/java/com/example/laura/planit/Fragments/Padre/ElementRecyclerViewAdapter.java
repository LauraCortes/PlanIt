package com.example.laura.planit.Fragments.Padre;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.List;

/**
 * Created by Usuario on 06/11/2016.
 */

abstract class ElementRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    List<Object> elementos;
    TabFragment tabFragment;
    public final  static int[] COLORES = {Color.parseColor("#26532B"), Color.parseColor("#6A0136"),
            Color.parseColor("#D72638"), Color.parseColor("#95C623"), Color.parseColor("#080708")};


    public ElementRecyclerViewAdapter(Context context, List<Object> elementos, TabFragment tabFragment)
    {
        this.elementos =elementos;
        this.context=context;
        this.tabFragment = tabFragment;
    }

    //TODO
    /**
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
        Contacto contacto = elementos.get(position);
        String nombre = contacto.getNombre();
        contactoRowViewHolder.textViewTitle.setText(nombre);
        contactoRowViewHolder.textViewSubtitle.setText(contacto.getNumeroTelefonico());
        Drawable iniciales =darImagenIniciales(nombre,position);
        contactoRowViewHolder.imgCirculo=iniciales;
        contactoRowViewHolder.circuloIniciales.setImageDrawable(iniciales);
        contactoRowViewHolder.decorarFavorito(contacto.isFavorito()==1);
        contactoRowViewHolder.decorarSeleccionado(tabFragment.isItemSelected(position));
        final ContactoRowViewHolder finalContactoRowViewHolder = contactoRowViewHolder;

        //Marcar contacto como Favorito
        contactoRowViewHolder.favorito.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Contacto contactoFavorito = elementos.get(position);
                Intent intent = new Intent(context, PersitenciaService.class);
                intent.putExtra("Requerimiento","MarcarContacto");
                intent.putExtra("Contacto", PlanIt.darInstancia().darContacto(position));
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
                if(tabFragment.modoEliminar())
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
     */

    @Override
    public int getItemCount() {
        if (elementos == null) {
            return 0;
        } else {
            return elementos.size();
        }
    }

    public Drawable darCirculoIniciales(String texto, int pos)
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
        return TextDrawable.builder().buildRound(iniciales.toUpperCase(), COLORES[pos%5]);

    }

    private void seleccionarItem(int position, RecyclerView.ViewHolder elementoRowViewHolder)
    {
        //TODO
        // boolean seleccionado = tabFragment.isItemSelected(position);
        //elementoRowViewHolder.decorarSeleccionado(!seleccionado);
        tabFragment.seleccionarItem(position);
    }

}
