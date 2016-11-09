package com.example.laura.planit.Activities.Contactos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.laura.planit.Modelos.Contacto;
import com.example.laura.planit.Modelos.PlanIt;
import com.example.laura.planit.Services.PersitenciaService;

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
            PlanIt.darInstancia().agregarContacto(contacto.getNombre(), contacto.getNumeroTelefonico());
            Intent intent = new Intent(this, PersitenciaService.class);
            intent.putExtra("Requerimiento", "AgregarContacto");
            intent.putExtra("Contacto", contacto);
            startService(intent);
        }
        setResult(contactosSeleccionados.size());
        contactosSeleccionados.clear();
        contactosSeleccionados = null;
        finish();
    }
}
