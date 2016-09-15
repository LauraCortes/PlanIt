package com.example.laura.planit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Laura on 12/09/2016.
 */
public class AgregarSitioActivity extends AppCompatActivity{

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbarAgregarSitio);
        setContentView(R.layout.activity_agregar_sitios);
        System.out.println("antes");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Agregar sitio Favorito");
        System.out.println("ahora");

    }


}
