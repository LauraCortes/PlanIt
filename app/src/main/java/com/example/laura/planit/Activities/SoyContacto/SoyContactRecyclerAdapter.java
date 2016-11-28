package com.example.laura.planit.Activities.SoyContacto;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Activities.Contactos.ContactoRowViewHolder;
import com.example.laura.planit.Fragments.ElementRecyclerViewAdapter;
import com.example.laura.planit.Fragments.ElementoRowViewHolder;
import com.example.laura.planit.Fragments.TabFragment;
import com.example.laura.planit.Modelos.Contacto;
import com.example.laura.planit.R;

import java.util.List;

/**
 * Created by Usuario on 06/11/2016.
 */

public class SoyContactRecyclerAdapter extends ElementRecyclerViewAdapter
{
    private Context context;
    public SoyContactRecyclerAdapter(Context context, List<Contacto> elementos, TabFragment tabFragment)
    {
        super(context, elementos, tabFragment);
        this.context=context;
    }
    public void swapData(List contactos)
    {
        elementos=contactos;
    }
    @Override
    public ElementoRowViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.tab_contactos_row_contacto, null);
        return  new ContactoRowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ElementoRowViewHolder elementoRowViewHolder, final int position)
    {
        final Contacto contacto = (Contacto)elementos.get(position);
        String nombre = contacto.getNombre();
        final SoyContactoRowViewHolder contactoRowViewHolder = (SoyContactoRowViewHolder)elementoRowViewHolder;
        contactoRowViewHolder.textViewTitle.setText(nombre);
        contactoRowViewHolder.textViewSubtitle.setText(contacto.getNumeroTelefonico());
        Drawable iniciales =darCirculoIniciales(nombre,position);
        contactoRowViewHolder.imgCirculo=iniciales;
        contactoRowViewHolder.circuloIniciales.setImageDrawable(iniciales);

        final SoyContactoRowViewHolder finalContactoRowViewHolder = contactoRowViewHolder;
        contactoRowViewHolder.vista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MovimientosActivity.class);
                intent.putExtra("numero_contacto",contacto.getNumeroTelefonico());
                context.startActivity(intent);
            }
        });

    }
}
