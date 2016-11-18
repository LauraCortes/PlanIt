package com.example.laura.planit.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.laura.planit.Activities.Main.LoginActivity;
import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Usuario on 06/11/2016.
 */

public abstract class TabFragment extends Fragment
{

    protected RecyclerView.Adapter adapter;
    public List elementos;
    protected List elementosSeleccionados;
    protected FloatingActionButton btnFAB;
    protected RecyclerView recyclerView;
    protected static int AGREGAR_ELEMENTOS =97;
    protected String msjToastAgregar="Elementos agregados";
    protected String celular;


    public TabFragment()
    {
        super();
    }


    public void cambiarIconoFAB()
    {
        if(elementosSeleccionados.size()!=0)
        {
            btnFAB.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.delete));
            btnFAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark)));
        }
        else
        {
            btnFAB.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.agregar));
            btnFAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(),R.color.colorAccent)));
        }
    }

    public void accionFAB(View v)
    {
        if(elementosSeleccionados.size()!=0)
        {
            eliminarElementosVista(v);
        }
        else
        {
            lanzarActivityAgregarElemento(v);
        }
    }

    protected abstract void lanzarActivityAgregarElemento(View view);

    //TODO convertir a abstract
    public void eliminarElementosVista(View view)
    {
        Iterator iterator = elementosSeleccionados.iterator();

        for (int i=0; iterator.hasNext();i++)
        {
            //TODO -> Eliminar del mundo
            elementos.remove(iterator.next());
        }
        elementosSeleccionados.clear();
        cambiarIconoFAB();
    }

    public void agregarItemEliminar(Object nuevo)
    {
        elementosSeleccionados.add(nuevo);
        cambiarIconoFAB();
    }

    public void removerItemEliminar(Object remover)
    {
        elementosSeleccionados.remove(remover);
        cambiarIconoFAB();
    }

    public boolean hayItemsSeleccionados()
    {
        return elementosSeleccionados.size()!=0;
    }

    public boolean isItemSelected(Object o)
    {
        return elementosSeleccionados.contains(o);
    }

    /**
     *
     * @param o Objeto a agregar
     * @return si el objeto ahora está seleccionado o no
     */
    public boolean seleccionarItem(Object o)
    {
        if(!isItemSelected(o))
        {
            agregarItemEliminar(o);
            System.out.println("Objeto añadido: "+o);
            return true;
        }
        else
        {
            removerItemEliminar(o);
            return false;
        }
    }

    public void deseleccionar()
    {
        if (!elementosSeleccionados.isEmpty())
        {
            elementosSeleccionados.clear();
            adapter.notifyDataSetChanged();
            cambiarIconoFAB();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == AGREGAR_ELEMENTOS) {
            // Make sure the request was successful
            if (resultCode != 0)
            {
                recyclerView.scrollToPosition(elementos.size()-1);
                Toast.makeText(getContext(),msjToastAgregar,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
