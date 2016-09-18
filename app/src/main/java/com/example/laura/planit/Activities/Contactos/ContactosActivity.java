package com.example.laura.planit.Activities.Contactos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.laura.planit.Activities.Sitios.AgregarSitioActivity;
import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.PersitenciaService;

/**
 * Created by Laura on 14/09/2016.
 */
public class ContactosActivity extends Activity{

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

    public void agregarContactos(View view)
    {
        Intent i = new Intent(this, AgregarContactoActivity.class);
        finish();
        startActivity(i);
    }

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
}
