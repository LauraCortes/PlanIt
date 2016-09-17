package com.example.laura.planit.Activities.Eventos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.laura.planit.Activities.Sitios.AgregarSitioActivity;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.R;

/**
 * Created by Usuario on 17/09/2016.
 */
public class MisEventosActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        if (PlanIt.darInstancia().darEventos().size() == 0) {
            Intent i = new Intent(this, AgregarEventoActivity.class);
            i.putExtra("editar", false);
            i.putExtra("titulo", "Agregar evento");
            finish();
            startActivity(i);
        }
        getSupportActionBar().setTitle("Mis eventos");
    }

}
