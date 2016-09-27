package com.example.laura.planit.Activities.Contactos;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.android.multiselector.SelectableHolder;
import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.R;

/**
 * Created by Usuario on 26/09/2016.
 */

public class ContactoRowViewHolder extends RecyclerView.ViewHolder
{
    TextView textViewTitle;
    TextView textViewSubtitle;
    ImageButton favorito;
    ImageView circuloIniciales;
    Drawable imgCirculo;


    View vista;
    public final  static int[] COLORES = {Color.parseColor("#26532B"), Color.parseColor("#6A0136"),
            Color.parseColor("#D72638"), Color.parseColor("#95C623"), Color.parseColor("#080708")};


    public ContactoRowViewHolder(View rowView)
    {
        super(rowView);
        vista=rowView;
        System.out.println("FALSE");
        textViewTitle=((TextView) rowView.findViewById(R.id.textViewNombre));
        textViewSubtitle=((TextView) rowView.findViewById(R.id.textViewTelefono));
        favorito=((ImageButton) rowView.findViewById(R.id.imageContactoFavorito));
        circuloIniciales=(ImageView)rowView.findViewById(R.id.circulo_iniciales);
    }

    public void decorarFavorito(boolean isFavorito)
    {
        if(isFavorito)
        {
            favorito.setBackgroundResource(R.drawable.estrella_verde);
            favorito.setAlpha((float) 1.0);
        }
        else
        {
            favorito.setBackgroundResource(R.drawable.estrella_gris);
            favorito.setAlpha((float) 0.3);
        }
    }


    public void decorarSeleccionado(boolean seleccionado)
    {
        if(seleccionado)
        {
            vista.setBackgroundColor(Color.parseColor("#BDBDBD"));
            circuloIniciales.setImageDrawable( vista.getResources().getDrawable(R.drawable.check_circulo_blanco));
        }
        else
        {
            circuloIniciales.setImageDrawable(imgCirculo);
            vista.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
