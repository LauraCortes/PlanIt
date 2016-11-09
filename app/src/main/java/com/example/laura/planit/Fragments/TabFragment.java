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
    protected HashMap elementosSeleccionados;
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


    public void eliminarElementosVista(View view)
    {
        Iterator<Map.Entry<Integer,Integer>> iterator = elementosSeleccionados.entrySet().iterator();

        for (int i=0; iterator.hasNext();i++)
        {

            Map.Entry<Integer, Integer> entrada = iterator.next();
            int pos = entrada.getKey()+i;
            //TODO -> Eliminar del mundo
            elementos.remove(pos);
            //Remueve del view
            adapter.notifyItemRemoved(pos);
        }
        elementosSeleccionados.clear();
        cambiarIconoFAB();
    }

    public void agregarItemEliminar(int pos)
    {
        elementosSeleccionados.put(pos,pos);
        cambiarIconoFAB();
    }

    public void removerItemEliminar(int pos)
    {
        elementosSeleccionados.remove(pos);
        cambiarIconoFAB();
    }

    public boolean hayItemsSeleccionados()
    {
        return elementosSeleccionados.size()!=0;
    }

    public boolean isItemSelected(int pos)
    {
        return elementosSeleccionados.containsKey(pos);
    }

    public void seleccionarItem(int pos)
    {
        if(!isItemSelected(pos))
        {
            agregarItemEliminar(pos);
        }
        else
        {
            removerItemEliminar(pos);
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
                obtenerElementos();
                System.out.println("Elementos en el tab -> "+elementos.size());
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(elementos.size()-1);
                Toast.makeText(getContext(),msjToastAgregar,Toast.LENGTH_SHORT).show();
            }
        }
    }

    public abstract void obtenerElementos();
}
