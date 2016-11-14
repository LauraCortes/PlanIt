package com.example.laura.planit.Activities.Eventos;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.example.laura.planit.Modelos.Contacto;
import com.example.laura.planit.Services.Constants;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
        List<Contacto> invitados = new ArrayList();
        for (Map.Entry<Integer, Contacto> entrada : contactosSeleccionados.entrySet())
        {
            invitados.add(entrada.getValue());
        }
        contactosSeleccionados.clear();
        Intent intent = new Intent();
        intent.putExtra(Constants.INVITADOS_EVENTO,(Serializable)invitados);
        setResult(invitados.size(),intent);
        finish();
    }
}
