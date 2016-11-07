package com.example.laura.planit.Activities.Contactos;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Fragments.Padre.ElementRecyclerViewAdapter;
import com.example.laura.planit.Fragments.Padre.ElementoRowViewHolder;
import com.example.laura.planit.Fragments.Padre.TabFragment;
import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.PersitenciaService;

import java.util.List;

/**
 * Created by Usuario on 06/11/2016.
 */

public class ContactRecyclerAdapterHerencia extends ElementRecyclerViewAdapter
{
    public ContactRecyclerAdapterHerencia(Context context, List<Contacto> elementos, TabFragment tabFragment)
    {
        super(context, elementos, tabFragment);
    }

    @Override
    public ElementoRowViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.row_contacto, null);
        return  new ContactoRowViewHolderHerencia(view);
    }

    @Override
    public void onBindViewHolder(final ElementoRowViewHolder elementoRowViewHolder, final int position)
    {
        Contacto contacto = (Contacto)elementos.get(position);
        String nombre = contacto.getNombre();
        final ContactoRowViewHolderHerencia contactoRowViewHolder = (ContactoRowViewHolderHerencia)elementoRowViewHolder;
        contactoRowViewHolder.textViewTitle.setText(nombre);
        contactoRowViewHolder.textViewSubtitle.setText(contacto.getNumeroTelefonico());
        Drawable iniciales =darCirculoIniciales(nombre,position);
        contactoRowViewHolder.imgCirculo=iniciales;
        contactoRowViewHolder.circuloIniciales.setImageDrawable(iniciales);
        contactoRowViewHolder.decorarFavorito(contacto.isFavorito()==1);
        contactoRowViewHolder.decorarSeleccionado(tabFragment.isItemSelected(position));
        final ContactoRowViewHolderHerencia finalContactoRowViewHolder = contactoRowViewHolder;

        //Marcar contacto como Favorito
        contactoRowViewHolder.favorito.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Contacto contactoFavorito = (Contacto)elementos.get(position);
                if(contactoFavorito.isFavorito()==0)
                {
                    finalContactoRowViewHolder.decorarFavorito(true);
                    contactoFavorito.setFavorito(1);
                }
                else
                {
                    finalContactoRowViewHolder.decorarFavorito(false);
                    contactoFavorito.setFavorito(0);
                }
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
}
