package com.example.laura.planit.Activities.Sitios;

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
import com.example.laura.planit.Modelos.Contacto;
import com.example.laura.planit.Modelos.PlanIt;
import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Laura on 12/09/2016.
 */
public class SitiosTabFragment extends TabFragment
{

    private  List<Sitio> sitios;
    public SitiosTabFragment(){
        super();
        AGREGAR_ELEMENTOS=125;
    }

    @Override
    protected void lanzarActivityAgregarElemento(View view)
    {
        Intent i = new Intent(getContext(), AgregarSitioActivity.class);
        i.putExtra("editar", false);
        i.putExtra("titulo", "Agregar sitio favorito");
        msjToastAgregar="Sitio agregado";
        startActivityForResult(i,AGREGAR_ELEMENTOS);
    }

    public List<Sitio> getSitios()
    {
        return sitios;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SharedPreferences properties = getContext().getSharedPreferences(getString(R.string.properties), Context.MODE_PRIVATE);
        if (properties.getBoolean(getString(R.string.logueado), false)) {
            celular = properties.getString(getString(R.string.usuario), "desconocido");
        } else {
            MainActivity.mostrarMensaje(getContext(), "Inicia sesión", "Tu sesión caducó, por favor vuelve a iniciar sesión");
            getActivity().finish();
            Intent i = new Intent(getContext(), LoginActivity.class);
            startActivity(i);
        }
        sitios = new ArrayList<Sitio>();
        elementosSeleccionados = new HashMap<Integer, Integer>();
        super.onCreateView(inflater, container, savedInstanceState);
        elementos= new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReferenceFromUrl(PlanIt.FIREBASE_URL).child("lugares_favoritos/" + celular);
        databaseReference.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Map<String, Sitio> map = (HashMap<String, Sitio>) dataSnapshot.getValue();
                        sitios= new ArrayList<Sitio>(map.values());
                        elementos=sitios;
                        ((SitioRecyclerViewAdapter)adapter).cambiarElementos(sitios);
                        System.out.println("Cambiaron los sitios, hay (adapter) " + adapter.getItemCount() + "-(elementos) "+elementos.size());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
        adapter = new SitioRecyclerViewAdapter(getContext(), (List<Sitio>) elementos, this);
        System.out.println("Cantidad de objetos en el adapter "+adapter.getItemCount());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tab_sitios, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewSitios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(adapter);
        btnFAB = (FloatingActionButton) view.findViewById(R.id.btnAgregarSitios);
        btnFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionFAB(v);
            }
        });
        return view;
    }
        /*databaseReference.addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s)
                    {
                        ((ArrayList<Sitio>)elementos).add(dataSnapshot.getValue(Sitio.class));
                        adapter.notifyItemInserted(elementos.size()-1);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        elementos.remove(dataSnapshot.getValue(Sitio.class));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        )
*/


    @Override
    public void obtenerElementos()
    {
        elementos=PlanIt.darInstancia().darSitios();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode>=-1)
        {
            if(resultCode==-1)
            {
                msjToastAgregar="Sitio agregado";
            }
            else
            {
                msjToastAgregar="Sitio editado";
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
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
