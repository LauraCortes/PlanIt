package com.example.laura.planit.Activities.Contactos;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.*;
import android.support.design.widget.FloatingActionButton;
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
import java.util.HashMap;
import java.util.List;
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
