package com.example.laura.planit.Fragments;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.laura.planit.R;

/**
 * Created by Usuario on 06/11/2016.
 */

public abstract class ElementoRowViewHolder extends RecyclerView.ViewHolder
{
    public View vista;

    public ElementoRowViewHolder(View itemView)
    {
        super(itemView);
        vista = itemView;
    }

    public void decorarSeleccionado(boolean seleccionado)
    {
        if(seleccionado)
        {
            //TODO acceder a los colores a través de R
            vista.setBackgroundColor(Color.parseColor("#45B29D"));
        }
        else
        {
            vista.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
