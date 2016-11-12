package com.example.laura.planit.Activities.Eventos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Fragments.TabFragment;
import com.example.laura.planit.Modelos.Evento;
import com.example.laura.planit.Modelos.PlanIt;
import com.example.laura.planit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Usuario on 02/11/2016.
 */

public class MisEventosTabFragment extends TabFragment
{
    public  MisEventosTabFragment()
    {
        super();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        elementosSeleccionados = new ArrayList();
        super.onCreate(savedInstanceState);
        elementos= PlanIt.darInstancia().darEventos();

        adapter=new EventoRecyclerViewAdapter(getActivity(), (List<Evento>)elementos,this);

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.tab_eventos, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(adapter);
        btnFAB= (FloatingActionButton) view.findViewById(R.id.fabAgregarEventos);
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
        Intent i = new Intent(getContext(), AgregarEventoActivity.class);
        i.putExtra("editar", false);
        i.putExtra("titulo", "Agregar evento");
        startActivity(i);
    }



}
