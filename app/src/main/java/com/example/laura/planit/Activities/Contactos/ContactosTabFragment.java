package com.example.laura.planit.Activities.Contactos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.R;

/**
 * Created by Laura on 14/09/2016.
 */
public class ContactosTabFragment extends Fragment
{
    public ContactosTabFragment()
    {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.activity_contactos, container, false);
        ListView listView = (ListView) view.findViewById(android.R.id.list);
        listView.setAdapter(new ContactAdapter(getActivity(), PlanIt.darInstancia().darContactos()));
        return view;
    }

    /**
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);
        if (PlanIt.darInstancia().darContactos().size() == 0) {
            Intent i = new Intent(this, AgregarContactoActivity.class);
            finish();
            startActivity(i);
        }
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new ContactAdapter(this,PlanIt.darInstancia().darContactos()));
    }
    **/

    public void agregarContactos(View view)
    {
        Intent i = new Intent(getActivity().getApplicationContext(), AgregarContactoActivity.class);
        //finish();
        startActivity(i);
    }

    /**
    public void eliminarContactos(View view)
    {
       ListView listView = (ListView) findViewById(android.R.id.list);
        for (int i = 0; i <listView.getAdapter().getCount() ; i++)
        {
            ViewGroup row = (ViewGroup) listView.getChildAt(i);
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

        listView.setAdapter(new ContactAdapter(this,PlanIt.darInstancia().darContactos()));
    }
     **/
}
