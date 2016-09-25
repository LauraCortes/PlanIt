package com.example.laura.planit.Activities.Sitios;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.R;

/**
 * Created by Laura on 12/09/2016.
 */
public class SitiosTabFragment extends Fragment
{
    public SitiosTabFragment(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_sitios, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewSitios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SitioRecyclerViewAdapter adapter = new SitioRecyclerViewAdapter(getActivity(), PlanIt.darInstancia().darSitios());
        recyclerView.setAdapter(adapter);
        return view;
    }

    /**
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitios);
        if (PlanIt.darInstancia().darSitios().size() == 0) {
            Intent i = new Intent(this, AgregarSitioActivity.class);
            i.putExtra("editar", false);
            i.putExtra("titulo", "Agregar primer sitio favorito");
            finish();
            startActivity(i);
        }
        getSupportActionBar().setTitle("Mis Sitios Favoritos");


    }

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewSitios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SitioRecyclerViewAdapter adapter = new SitioRecyclerViewAdapter(this, PlanIt.darInstancia().darSitios());
        recyclerView.setAdapter(adapter);
    }

    public void agregarSitio(View view) {
        Intent i = new Intent(this, AgregarSitioActivity.class);
        i.putExtra("editar", false);
        i.putExtra("titulo", "Agregar sitio favorito");
        startActivity(i);
    }
    */


}
