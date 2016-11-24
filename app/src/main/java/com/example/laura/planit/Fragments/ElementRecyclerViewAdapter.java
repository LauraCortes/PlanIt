package com.example.laura.planit.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.Collection;
import java.util.List;

/**
 * Created by Usuario on 06/11/2016.
 */

public abstract class ElementRecyclerViewAdapter extends RecyclerView.Adapter<ElementoRowViewHolder>
{
    protected Context context;
    protected List elementos;
    protected TabFragment tabFragment;
    public final  static int[] COLORES = {Color.parseColor("#26532B"), Color.parseColor("#6A0136"),
            Color.parseColor("#D72638"), Color.parseColor("#95C623"), Color.parseColor("#080708")};

    public void swapData(List nuevosElementos)
    {
        elementos=nuevosElementos;
    }

    public ElementRecyclerViewAdapter(Context context, List elementos, TabFragment tabFragment)
    {
        this.elementos =elementos;
        this.context=context;
        this.tabFragment = tabFragment;
    }

    @Override
    public int getItemCount() {
        if (elementos == null) {
            return 0;
        } else {
            return elementos.size();
        }
    }

    public static Drawable darCirculoIniciales(String texto, int pos)
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

    protected void seleccionarItem(Object elemento, ElementoRowViewHolder elementoRowViewHolder)
    {
        boolean seleccionado = tabFragment.seleccionarItem(elemento);
        elementoRowViewHolder.decorarSeleccionado(seleccionado);
    }

}
