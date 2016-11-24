package com.example.laura.planit.Activities.Contactos;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.laura.planit.Fragments.AgregarSuper;
import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Modelos.Contacto;
import com.example.laura.planit.R;
import com.example.laura.planit.Activities.Main.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Laura on 17/09/2016.
 */
public class AgregarContactoActivity extends AgregarSuper
{
    private Context contexto;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        contexto = this;
        titulo="Agregar contactos de emergencia";
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_agregar_contacto);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_agregar_contacto));
        getSupportActionBar().setTitle("Agregar contactos de emergencia");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.atras_icon);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        leerContactos();
        btnFAB=(FloatingActionButton)findViewById(R.id.btnAgregarSitios);
        listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(new AgregarContactoAdapter(this, contactos));
        contactosSeleccionados = new HashMap<Integer, Contacto>();
        cambiarIconoFAB();
    }

    public void agregar(View view)
    {
        for (Map.Entry<Integer, Contacto> entrada : contactosSeleccionados.entrySet()) {
            final Contacto contacto = entrada.getValue();
            SharedPreferences properties = this.getSharedPreferences(getString(R.string.properties), Context.MODE_PRIVATE);
            if (properties.getBoolean(getString(R.string.logueado), false)) {
                final String celular = properties.getString(getString(R.string.usuario), "desconocido");
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child(contacto.darRutaElemento(celular));
                databaseReference.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
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
                MainActivity.mostrarMensaje(this, "Error", "Parece que no has iniciado sesi?n. Intenta cerrar sesi?n e ingresar de nuevo");
            }
        }
        setResult(contactosSeleccionados.size());
        contactosSeleccionados.clear();
        finish();
}
}