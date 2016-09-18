package com.example.laura.planit.Activities.Contactos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.laura.planit.Activities.Sitios.AgregarSitioActivity;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.R;

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
        listView.setAdapter(new AddContactAdapter(this,PlanIt.darInstancia().darContactos()));
    }

    public void agregarContactos(View view)
    {
        Intent i = new Intent(this, AgregarContactoActivity.class);
        finish();
        startActivity(i);
    }

    public void eliminarContactos(View view)
    {

    }
}
