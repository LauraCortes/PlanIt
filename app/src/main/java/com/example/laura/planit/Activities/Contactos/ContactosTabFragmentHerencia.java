package com.example.laura.planit.Activities.Contactos;

/**
 * Created by Usuario on 06/11/2016.
 */

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Fragments.Padre.*;
import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.R;

import java.util.HashMap;
import java.util.List;

public class ContactosTabFragmentHerencia extends TabFragment
{
    public ContactosTabFragmentHerencia()
    {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        elementosSeleccionados = new HashMap<Integer, Integer>();
        super.onCreate(savedInstanceState);
        elementos= PlanIt.darInstancia().darContactos();

        adapter=new ContactRecyclerAdapterHerencia(getActivity(), (List<Contacto>)elementos,this);

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.activity_contactos, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(android.R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(adapter);
        btnFAB= (FloatingActionButton) view.findViewById(R.id.fabContactos);
        btnFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionFAB(v);
            }
        });
        return view;
    }

    @Override
    protected void lanzarActivityAgregarElemento(View view)
    {
        ((MainActivity)getActivity()).agregarContactos(view);
    }
}
