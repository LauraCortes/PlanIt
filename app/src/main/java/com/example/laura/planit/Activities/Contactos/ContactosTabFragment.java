package com.example.laura.planit.Activities.Contactos;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bignerdranch.android.multiselector.MultiSelector;
import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Activities.Sitios.SitioRecyclerViewAdapter;
import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.laura.planit.R.id.view;

/**
 * Created by Laura on 14/09/2016.
 */
public class ContactosTabFragment extends Fragment implements View.OnKeyListener
{
    private ContactRecyclerViewAdapter contactAdapter;
    private List<Contacto> contactos;
    private HashMap<Integer,Contacto> contactosEliminar;
    private FloatingActionButton btnFAB;
    private RecyclerView recyclerView;


    public ContactosTabFragment()
    {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        contactos=PlanIt.darInstancia().darContactos();
        contactosEliminar = new HashMap<Integer, Contacto>();
        contactAdapter=new ContactRecyclerViewAdapter(getActivity(), contactos,this);

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.activity_contactos, container, false);
        this.recyclerView = (RecyclerView) view.findViewById(android.R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerView.setAdapter(contactAdapter);

        btnFAB= (FloatingActionButton) view.findViewById(R.id.fabContactos);
        btnFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accionFAB(v);
            }
        });
        return view;
    }

    public void cambiarIconoFAB()
    {
        if(contactosEliminar.size()!=0)
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
        if(contactosEliminar.size()!=0)
        {
            eliminarContactos(v);
        }
        else
        {
            ((MainActivity)getActivity()).agregarContactos(v);
        }
    }


    public void eliminarContactos(View view)
    {
        for (Map.Entry<Integer, Contacto> entrada : contactosEliminar.entrySet())
        {
            int pos = entrada.getKey();
            //Elimina del mundo
            contactos.remove(pos);
            //Remueve del view
            contactAdapter.notifyItemRemoved(pos);
            //Saca de la lista a eliminar
            contactosEliminar.remove(Integer.valueOf(pos));
        }
        cambiarIconoFAB();
        /**
       ListView recyclerView = (ListView) findViewById(android.R.id.list);
        for (int i = 0; i <recyclerView.getAdapter().getCount() ; i++)
        {
            ViewGroup row = (ViewGroup) recyclerView.getChildAt(i);
            CheckBox tvTest = (CheckBox) row.findViewById(R.id.checkBox);
            //  Get your controls from this ViewGroup and perform your task on them =)
            if (tvTest.isChecked())
            {
                // DO SOMETHING
                TextView tN= (TextView) row.findViewById(R.id.textViewTelefono);
                PlanIt.darInstancia().eliminarContacto(i);
                Intent intent = new Intent(this, PersitenciaService.class);
                intent.putExtra("Requerimiento","EliminarContacto");
                intent.putExtra("Telefono",tN.getText().toString());
                startService(intent);

            }
        }

        recyclerView.setAdapter(new ContactAdapter(this,PlanIt.darInstancia().darContactos()));
         */
    }


    public boolean onKey(View view, int keyCode, KeyEvent event) {
        switch (keyCode)
        {
            case KeyEvent.KEYCODE_BACK:
                //TODO cancelar borrado
                return true;
        }
        return false;
    }

    public void agregarItemEliminar(int pos)
    {
        Contacto contacto= contactos.get(pos);
        contactosEliminar.put(pos,contacto);
        System.out.println("Agregado:"+contacto.getNombre()+" ahora:"+contactosEliminar.size());
        cambiarIconoFAB();
        contacto=null;
    }

    public void removerItemEliminar(int pos)
    {
        contactosEliminar.remove(pos);
        System.out.println("Eliminado:"+contactos.get(pos).getNombre()+" ahora:"+contactosEliminar.size());
        cambiarIconoFAB();
    }

    public boolean modoEliminar()
    {
        return contactosEliminar.size()!=0;
    }

    public boolean isItemSelected(int pos)
    {
        return contactosEliminar.containsKey(pos);
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

}
