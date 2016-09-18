package com.example.laura.planit.Activities.Transportes;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.example.laura.planit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 18/09/2016.
 */
public class AgregarTransporteActivity extends AppCompatActivity
{
    RadioButton radioTaxi, radioUber, radioBus, radioCarro;
    List<RadioButton> radios;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_transporte);
        radioBus=(RadioButton) findViewById(R.id.radioTransportePublico);

        radioTaxi=(RadioButton) findViewById(R.id.radioTaxi);
        radioTaxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desactivarRadios(radioTaxi);
            }
        });
        radioUber=(RadioButton) findViewById(R.id.radioUber);
        radioUber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                desactivarRadios(radioUber);
            }
        });
        radioCarro=(RadioButton) findViewById(R.id.radioCarro);
        radioCarro.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                        desactivarRadios(radioCarro);
                }
            }
        );
        radios=new ArrayList<RadioButton>();
        radios.add(radioBus);
        radios.add(radioTaxi);
        radios.add(radioUber);
        radios.add(radioBus);

        getSupportActionBar().setTitle("Regreso del evento");
    }

    public void desactivarRadios(View originador)
    {
        for(RadioButton radio: radios)
        {
            if(radio!=originador)
            {
                radio.setChecked(false);
            }
            else
            {
                radio.setChecked(true);
            }
        }

    }
}
