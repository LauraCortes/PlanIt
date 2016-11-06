package com.example.laura.planit.Activities.Eventos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Activities.Contactos.ContactRecyclerViewAdapter;
import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.Logica.Evento;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Usuario on 02/11/2016.
 */

public class MisEventosTabFragment extends Fragment
{
//    private EventoRecyclerViewAdapter eventoAdapter;
//    private List<Evento> eventos;
//    private HashMap<Integer,Integer> eventosEliminar;
//    private FloatingActionButton btnFAB;
//    private RecyclerView recyclerView;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//
//        eventos=PlanIt.darInstancia().darEventos();
//        eventosEliminar = new HashMap<Integer, Integer>();
//        eventoAdapter=new EventoRecyclerViewAdapter(getActivity(), eventos,this);
//
//        View view=inflater.inflate(R.layout.activity_eventos, container, false);
//        this.recyclerView = (RecyclerView) view.findViewById(android.R.id.recyclerViewEventos);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        this.recyclerView.setAdapter(eventoAdapter);
//        btnFAB= (FloatingActionButton) view.findViewById(R.id.fabContactos);
//        btnFAB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                accionFAB(v);
//            }
//        });
//        return view;
//    }
}
