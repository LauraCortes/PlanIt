package com.example.laura.planit.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
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
            vista.setBackgroundColor(ContextCompat.getColor(vista.getContext(),R.color.colorSelectedRow));
        }
        else
        {
            vista.setBackgroundColor(Color.TRANSPARENT);
        }
    }
}
