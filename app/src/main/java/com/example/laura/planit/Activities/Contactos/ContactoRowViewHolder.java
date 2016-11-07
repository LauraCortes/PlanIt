package com.example.laura.planit.Activities.Contactos;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laura.planit.Fragments.ElementoRowViewHolder;
import com.example.laura.planit.R;

/**
 * Created by Usuario on 06/11/2016.
 */

public class ContactoRowViewHolder extends ElementoRowViewHolder
{
    TextView textViewTitle;
    TextView textViewSubtitle;
    ImageButton favorito;
    ImageView circuloIniciales;
    Drawable imgCirculo;

    public ContactoRowViewHolder(View itemView) {
        super(itemView);
        textViewTitle=((TextView) itemView.findViewById(R.id.textViewNombre));
        textViewSubtitle=((TextView) itemView.findViewById(R.id.textViewTelefono));
        favorito=((ImageButton) itemView.findViewById(R.id.imageContactoFavorito));
        circuloIniciales=(ImageView)itemView.findViewById(R.id.circulo_iniciales);
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
        super.decorarSeleccionado(seleccionado);
        if(seleccionado)
        {
            circuloIniciales.setImageDrawable( vista.getResources().getDrawable(R.drawable.check_border_48));
        }
        else
        {
            circuloIniciales.setImageDrawable(imgCirculo);
        }
    }
}
