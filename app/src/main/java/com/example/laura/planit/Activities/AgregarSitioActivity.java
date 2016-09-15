package com.example.laura.planit.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.R;

/**
 * Created by Laura on 12/09/2016.
 */
public class AgregarSitioActivity extends AppCompatActivity{

    EditText txtNombre, txtBarrio, txtDireccion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_sitios);
        //setSupportActionBar(((Toolbar)findViewById(R.id.toolbarAgregarSitio) ));
        getSupportActionBar().setTitle("Agregar sitio favorito");
        txtNombre=(EditText)findViewById(R.id.txtNombreSitioFavorito);
        txtBarrio=(EditText)findViewById(R.id.txtBarrioFavorito);
        txtDireccion=(EditText)findViewById(R.id.txtDireccionFavorito);
    }

    public void agregarSitio(View view)
    {
        PlanIt.darInstancia().agregarSitio(txtNombre.getText().toString(),txtBarrio.getText().toString(),txtDireccion.getText().toString());
        Toast.makeText(this, "Tu sitio se agregó", Toast.LENGTH_SHORT).show();
        finish();
    }


}
