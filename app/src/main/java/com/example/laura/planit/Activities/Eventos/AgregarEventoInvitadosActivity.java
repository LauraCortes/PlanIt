package com.example.laura.planit.Activities.Eventos;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import com.example.laura.planit.Activities.Contactos.AgregarContactoAdapter;
import com.example.laura.planit.Fragments.AgregarSuper;
import com.example.laura.planit.Modelos.Contacto;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.Constants;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Laura on 18/09/2016.
 */
public class AgregarEventoInvitadosActivity extends AgregarSuper
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        titulo="Selecciona tus invitados";
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_invitar_contactos);
        //TODO no funciona el cargue de contactos

        //ToolBar
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar_agregar_invitados));
        getSupportActionBar().setTitle("Selecciona tus invitados");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.atras_icon);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        leerContactos();
        btnFAB=(FloatingActionButton)findViewById(R.id.buttonInvitarEvento);
        listView = (ListView) findViewById(R.id.lista_contactos_evento);
        listView.setAdapter(new AgregarContactoAdapter(this, contactos));
        contactosSeleccionados = new HashMap<Integer, Contacto>();
        cambiarIconoFAB();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void agregar(View view)
    {
        if(contactosSeleccionados.size()>0)
        {
            List<Contacto> invitadosEvento = new ArrayList();
            for (Map.Entry<Integer, Contacto> entrada : contactosSeleccionados.entrySet())
            {
                invitadosEvento.add(entrada.getValue());
            }
            contactosSeleccionados.clear();
            Intent intent = new Intent();
            intent.putExtra(Constants.INVITADOS_EVENTO,(Serializable)invitadosEvento);
            setResult(RESULT_OK,intent);
            finish();
        }
        else
        {
            Toast.makeText(this,"Debe seleccionar algún invitado",Toast.LENGTH_SHORT).show();
        }

    }
}
