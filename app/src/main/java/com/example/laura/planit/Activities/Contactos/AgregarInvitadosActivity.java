package com.example.laura.planit.Activities.Contactos;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;


import com.example.laura.planit.Logica.Contacto;

import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.PersitenciaService;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Laura on 18/09/2016.
 */
public class AgregarInvitadosActivity extends AgregarSuper {

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
