package com.example.laura.planit.Activities.Eventos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.example.laura.planit.Modelos.Contacto;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Laura on 18/09/2016.
 */
public class AgregarInvitadosActivity extends AgregarSuper
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        titulo="Selecciona tus invitados";
        super.onCreate(savedInstanceState);
    }

    public void agregar(View view)
    {
        ArrayList<Contacto> invitados = new ArrayList<Contacto>();
        for (Map.Entry<Integer, Contacto> entrada : contactosSeleccionados.entrySet())
        {
            invitados.add(entrada.getValue());
        }
        contactosSeleccionados.clear();
        contactosSeleccionados=null;
        Intent intent = new Intent();
        intent.putExtra("Invitados",(Serializable)invitados);
        setResult(invitados.size(),intent);
        finish();
    }
}
