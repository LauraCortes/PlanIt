package com.example.laura.planit.Activities.Transportes;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;

import com.example.laura.planit.R;

/**
 * Created by Usuario on 18/09/2016.
 */
public class AgregarTransporteActivity extends AppCompatActivity
{
    RadioButton radioTaxi, radioUber, radioBus, radioCarro;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_transporte);
        radioBus=(RadioButton)findViewById(R.id.radioTransportePublico);
        radioTaxi=(RadioButton)findViewById(R.id.radioTaxi);
        radioUber=(RadioButton)findViewById(R.id.radioUber);
        radioCarro=(RadioButton)findViewById(R.id.radioCarro);
        getSupportActionBar().setTitle("Regreso del evento");
    }
}
