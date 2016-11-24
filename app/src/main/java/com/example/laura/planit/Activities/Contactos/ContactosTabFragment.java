package com.example.laura.planit.Activities.Contactos;

/**
 * Created by Usuario on 06/11/2016.
 */

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

public class ContactosTabFragment extends TabFragment
{
    public ContactosTabFragment()
    {
        super();
        AGREGAR_ELEMENTOS=654;
        msjToastAgregar="Contactos agregados";
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
        //TODO traer los contactos de la db
        elementos= new ArrayList<>();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child("contacto_emergencia/" + celular);
        databaseReference.keepSynced(true);
        ValueEventListener valueEventListener = databaseReference.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        System.out.println("Los contactos cambiaron");
                        GenericTypeIndicator<HashMap<String,Contacto>> t = new GenericTypeIndicator<HashMap<String, Contacto>>(){};
                        HashMap<String, Contacto> map =dataSnapshot.getValue(t);

                        if(map!=null)
                        {
                            ArrayList<Contacto> nuevos = new ArrayList(map.values());
                            elementos=nuevos;
                            ((ContactRecyclerAdapter)adapter).swapData(nuevos);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
        adapter = new ContactRecyclerAdapter(getContext(), elementos, this);

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.tab_contactos, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(R.id.listViewContactos);
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
        Intent intent = new Intent(getContext(), AgregarContactoActivity.class);
        startActivityForResult(intent, AGREGAR_ELEMENTOS);
    }
}
