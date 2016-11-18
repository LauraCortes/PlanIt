package com.example.laura.planit.Activities.Contactos;

/**
 * Created by Usuario on 06/11/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.laura.planit.Fragments.TabFragment;
import com.example.laura.planit.Modelos.Contacto;
import com.example.laura.planit.R;

import java.util.ArrayList;
import java.util.List;

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
        elementosSeleccionados = new ArrayList();
        super.onCreate(savedInstanceState);
        //TODO traer los contactos de la db
        //elementos= PlanIt.darInstancia().darContactos();

        adapter=new ContactRecyclerAdapter(getActivity(), (List<Contacto>)elementos,this);

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
