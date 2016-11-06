package com.example.laura.planit.Activities.Main;


import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Usuario on 06/11/2016.
 */

abstract class FragmentSuper extends Fragment
{

    protected RecyclerView.Adapter adapter;
    protected List<Object> elementos;
    private HashMap<Integer,Integer> elementosSeleccionados;
    private FloatingActionButton btnFAB;
    private RecyclerView recyclerView;


    public FragmentSuper()
    {
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
            lanzarActivityAgregarElemento();
        }
    }

    abstract void lanzarActivityAgregarElemento();


    public void eliminarElementosVista(View view)
    {
        for (Map.Entry<Integer, Integer> entrada : elementosSeleccionados.entrySet())
        {
            int pos = entrada.getKey();
            //Elimina del mundo
            elementos.remove(pos);
            //Remueve del view
            adapter.notifyItemRemoved(pos);
            //Saca de la lista a eliminar
            elementosSeleccionados.remove(Integer.valueOf(pos));
        }
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

    public boolean modoEliminar()
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

    public void deseleccionarOnBack()
    {
        if (!elementosSeleccionados.isEmpty()) {
            elementosSeleccionados.clear();
            adapter.notifyDataSetChanged();
            cambiarIconoFAB();
        }
    }
}
