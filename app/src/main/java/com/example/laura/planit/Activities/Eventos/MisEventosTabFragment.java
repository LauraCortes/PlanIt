package com.example.laura.planit.Activities.Eventos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Activities.Main.LoginActivity;
import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Fragments.TabFragment;
import com.example.laura.planit.Modelos.ResumenEvento;
import com.example.laura.planit.R;
import com.example.laura.planit.Activities.Main.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

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
        SharedPreferences properties = getContext().getSharedPreferences(getString(R.string.properties), Context.MODE_PRIVATE);
        if (properties.getBoolean(getString(R.string.logueado), false)) {
            celular = properties.getString(getString(R.string.usuario), "desconocido");
        } else {
            MainActivity.mostrarMensaje(getContext(), "Inicia sesión", "Tu sesión caducó, por favor vuelve a iniciar sesión");
            getActivity().finish();
            Intent i = new Intent(getContext(), LoginActivity.class);
            startActivity(i);
        }

        elementosSeleccionados = new ArrayList();
        super.onCreate(savedInstanceState);
        elementos= new ArrayList();
        adapter=new MiEventoRecyclerViewAdapter(getActivity(), (List<ResumenEvento>)elementos,this);

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child(Constants.URL_MIS_EVENTOS + celular);
        databaseReference.keepSynced(true);
        databaseReference.orderByChild("fecha");
        //TODO - hacer que desde la DB solo se puedan leer las últimas 36 horas;
        long ultimas36Horas = System.currentTimeMillis()-(129600000);
        databaseReference.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        GenericTypeIndicator<HashMap<String,ResumenEvento>> t = new GenericTypeIndicator<HashMap<String, ResumenEvento>>(){};
                        HashMap<String, ResumenEvento> map =dataSnapshot.getValue(t);
                        if(map!=null)
                        {
                            ArrayList<ResumenEvento> nuevos = new ArrayList(map.values());
                            elementos=nuevos;
                            ((MiEventoRecyclerViewAdapter)adapter).swapData(nuevos);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

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
