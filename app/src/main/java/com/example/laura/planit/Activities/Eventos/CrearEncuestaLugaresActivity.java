package com.example.laura.planit.Activities.Eventos;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.laura.planit.Activities.Contactos.AgregarContactoAdapter;
import com.example.laura.planit.Activities.Sitios.AgregarSitioActivity;
import com.example.laura.planit.Activities.Sitios.SitioEncuestaRecyclerViewAdapter;
import com.example.laura.planit.Activities.Sitios.SitioRecyclerViewAdapter;
import com.example.laura.planit.Modelos.Contacto;
import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Usuario on 22/11/2016.
 */

public class CrearEncuestaLugaresActivity extends AppCompatActivity
{
    private List<Sitio> sitios;
    private Context contexto;
    protected HashMap<String, Sitio> sitiosSeleccionados;
    protected RecyclerView listView;
    protected Button btnFAB;
    private SitioEncuestaRecyclerViewAdapter adapter;

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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contexto = this;
        setContentView(R.layout.activity_agregar_encuesta_lugares);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_agregar_encuesta));
        getSupportActionBar().setTitle("Seleccionar sitios para votación");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.atras_icon);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        leerSitios();

        btnFAB = (Button) findViewById(R.id.btnAgregarEncuesta);
        adapter = new SitioEncuestaRecyclerViewAdapter(sitios, contexto, this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView = (RecyclerView) findViewById(R.id.recycler_sitios_encuesta);
        listView.setLayoutManager(llm);
        listView.setAdapter(adapter);
        sitiosSeleccionados = new HashMap<>();
        cambiarIconoFAB();
    }

    public boolean seleccionarItem(Sitio sitio) {
        boolean estado = false;
        if (sitiosSeleccionados.containsKey(sitio.getNombre())) {
            sitiosSeleccionados.remove(sitio.getNombre());
        } else {
            sitiosSeleccionados.put(sitio.getNombre(), sitio);
            estado = true;
        }
        cambiarIconoFAB();
        return estado;
    }

    public boolean estaSeleccionado(String nombre_sitio) {
        return sitiosSeleccionados.containsKey(nombre_sitio);
    }

    public void cambiarIconoFAB() {
        if (sitiosSeleccionados.size() > 0) {
            btnFAB.setVisibility(View.VISIBLE);
        } else {
            btnFAB.setVisibility(View.GONE);
        }
    }

    public void agregar(View view) {
        if (sitiosSeleccionados.size() > 1)
        {
            Intent intent = new Intent(this, AgregarInvitadosActivity.class);
            startActivityForResult(intent, AgregarEventoActivity.INVITAR_AMIGOS);

        } else {
            Toast.makeText(contexto, "Debe seleccionar más de un sitio para que los participantes " +
                    "voten", Toast.LENGTH_SHORT).show();
        }
    }


    private void leerSitios() {
        SharedPreferences properties = this.getSharedPreferences(getString(R.string.properties), Context.MODE_PRIVATE);
        if (properties.getBoolean(getString(R.string.logueado), false)) {
            String celular = properties.getString(getString(R.string.usuario), "desconocido");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child(Constants.URL_LUGARES_FAVORITOS + celular);
            databaseReference.addValueEventListener(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            GenericTypeIndicator<HashMap<String, Sitio>> t = new GenericTypeIndicator<HashMap<String, Sitio>>() {
                            };
                            HashMap<String, Sitio> map = dataSnapshot.getValue(t);
                            if (map != null) {
                                ArrayList<Sitio> nuevos = new ArrayList(map.values());
                                sitios = nuevos;
                                adapter.swapData(sitios);
                                adapter.notifyDataSetChanged();
                            } else {
                                sitios = new ArrayList<>();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );
        }

    }


    public void agregarSitioEncuesta(View v)
    {
        Intent i = new Intent(contexto, AgregarSitioActivity.class);
        i.putExtra("editar", false);
        i.putExtra("titulo", "Agregar sitio favorito");
        contexto.startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode==RESULT_OK )
        {
            List<Sitio> sitiosArray = new ArrayList();
            for (Map.Entry<String, Sitio> entrada : sitiosSeleccionados.entrySet())
            {
                sitiosArray.add(entrada.getValue());
            }
            Intent i = new Intent();
            i.putExtra(Constants.EXTRA_SITIOS_EVENTO, (Serializable) sitiosArray);
            i.putExtra(Constants.INVITADOS_EVENTO,data.getSerializableExtra(Constants.INVITADOS_EVENTO));
            setResult(RESULT_OK, i);
            finish();
        }
    }
}
