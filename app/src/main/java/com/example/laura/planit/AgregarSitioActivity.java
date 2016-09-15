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
        setContentView(R.layout.activity_agregar_sitios);
        getSupportActionBar().setTitle("Agregar sitio favorito");



    }


}
