package com.example.laura.planit.Activities.Contactos;

import android.os.Bundle;
import android.view.View;

import com.example.laura.planit.Activities.Eventos.AgregarSuper;
import com.example.laura.planit.Modelos.Contacto;

import java.util.Map;

/**
 * Created by Laura on 17/09/2016.
 */
public class AgregarContactoActivity extends AgregarSuper
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        titulo="Agregar contactos de emergencia";
        super.onCreate(savedInstanceState);
    }

    public void agregar(View view)
    {
        for (Map.Entry<Integer, Contacto> entrada : contactosSeleccionados.entrySet())
        {
            Contacto contacto = entrada.getValue();
            //TODO agregar en la DB
        }
        setResult(contactosSeleccionados.size());
        contactosSeleccionados.clear();
        finish();
    }
}
