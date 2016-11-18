package com.example.laura.planit.Activities.Contactos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.laura.planit.Activities.Eventos.AgregarSuper;
import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Modelos.Contacto;
import com.example.laura.planit.Modelos.PlanIt;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.PersitenciaService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by Laura on 17/09/2016.
 */
public class AgregarContactoActivity extends AgregarSuper
{
    private Context contexto;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        contexto = this;
        titulo="Agregar contactos de emergencia";
        super.onCreate(savedInstanceState);
    }

    public void agregar(View view)
    {
        for (Map.Entry<Integer, Contacto> entrada : contactosSeleccionados.entrySet())
        {
            final Contacto contacto = entrada.getValue();
            SharedPreferences properties = this.getSharedPreferences(getString(R.string.properties), Context.MODE_PRIVATE);
            if (properties.getBoolean(getString(R.string.logueado), false))
            {
                final String celular = properties.getString(getString(R.string.usuario), "desconocido");
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = database.getReferenceFromUrl(PlanIt.FIREBASE_URL).child(contacto.darRutaElemento(celular));
                databaseReference.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                if (dataSnapshot.exists()) {
                                    MainActivity.mostrarMensaje(contexto, "Sitio existente", "Ya existe" +
                                            " un sitio con este nombre");
                                } else
                                {
                                    databaseReference.setValue(contacto);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                MainActivity.mostrarMensaje(contexto, "Error", databaseError.getMessage());
                                databaseError.toException().printStackTrace();
                            }
                        }
                );
                finish();
            } else {
                MainActivity.mostrarMensaje(this, "Error", "Parece que no has iniciado sesión. Intenta cerrar sesión e ingresar de nuevo");
            }
        }
        setResult(contactosSeleccionados.size());
        contactosSeleccionados.clear();
        contactosSeleccionados = null;
        finish();
    }
}
