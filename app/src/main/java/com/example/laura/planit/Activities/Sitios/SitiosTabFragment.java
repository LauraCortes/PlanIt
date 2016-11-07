package com.example.laura.planit.Activities.Sitios;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Activities.Eventos.EventoRecyclerViewAdapter;
import com.example.laura.planit.Fragments.TabFragment;
import com.example.laura.planit.Logica.Evento;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.Logica.Sitio;
import com.example.laura.planit.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Laura on 12/09/2016.
 */
public class SitiosTabFragment extends TabFragment
{
    public SitiosTabFragment(){}

    @Override
    protected void lanzarActivityAgregarElemento(View view)
    {
        Intent i = new Intent(getContext(), AgregarSitioActivity.class);
        i.putExtra("editar", false);
        i.putExtra("titulo", "Agregar sitio favorito");
        startActivity(i);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        elementosSeleccionados = new HashMap<Integer, Integer>();
        super.onCreateView(inflater,container,savedInstanceState);
        elementos= PlanIt.darInstancia().darSitios();
        adapter=new SitioRecyclerViewAdapter(getContext(), (List<Sitio>)elementos, this);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_sitios, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewSitios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(adapter);
        btnFAB= (FloatingActionButton) view.findViewById(R.id.btnAgregarSitios);
        btnFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionFAB(v);
            }
        });
        return view;
    }


    /**
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_sitios);
        if (PlanIt.darInstancia().darSitios().size() == 0) {
            Intent i = new Intent(this, AgregarSitioActivity.class);
            i.putExtra("editar", false);
            i.putExtra("titulo", "Agregar primer sitio favorito");
            finish();
            startActivity(i);
        }
        getSupportActionBar().setTitle("Mis Sitios Favoritos");


    }

    */


}
