package com.example.laura.planit.Activities.Sitios;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Modelos.PlanIt;
import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.PersitenciaService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Laura on 12/09/2016.
 */
public class AgregarSitioActivity extends AppCompatActivity{

    private EditText txtNombre,txtDireccion;
    private boolean editar;
    private int pos;
    private String nombre;
    Context contexto;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contexto=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_sitios);
        txtNombre=(EditText)findViewById(R.id.txtNombreSitioFavorito);
        txtDireccion=(EditText)findViewById(R.id.txtDireccionFavorito);
        Intent intent = getIntent();
        editar = intent.getExtras().getBoolean("editar");
        if(editar)
        {
            pos=intent.getIntExtra("posicion",-1);
            if(pos!=-1)
            {
                Sitio sitioEditado = PlanIt.darInstancia().darSitios().get(pos);
                nombre=sitioEditado.getNombre();
                txtNombre.setText(nombre);
                txtDireccion.setText(sitioEditado.getDirección());
                sitioEditado=null;
            }

        }
        //getSupportActionBar().setTitle(intent.getStringExtra("titulo"));
        intent=null;
    }

    public void agregarSitio(View view) {
        String nombre, barrio, direccion;
        nombre = txtNombre.getText().toString().trim();
        direccion = txtDireccion.getText().toString().trim();
        if (nombre.isEmpty()) {
            txtNombre.requestFocus();
            txtNombre.setError("Debe llenar este campo");
        } else {
            txtNombre.setError(null);
            if (direccion.isEmpty()) {
                txtDireccion.requestFocus();
                txtDireccion.setError("Debe llenar este campo");
            } else
            {
                txtDireccion.setError(null);
                //public Sitio(int latitud, int longitud, String nombre, String dirección) {
                final Sitio nuevoSitio = new Sitio();
                nuevoSitio.setDirección(direccion);
                nuevoSitio.setNombre(nombre);

                SharedPreferences properties = this.getSharedPreferences(getString(R.string.properties), Context.MODE_PRIVATE);
                if (properties.getBoolean(getString(R.string.logueado), false))
                {
                    String celular = properties.getString(getString(R.string.usuario), "desconocido");
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference databaseReference = database.getReferenceFromUrl(PlanIt.FIREBASE_URL).child(nuevoSitio.darRutaElemento(celular));
                    databaseReference.addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists())
                                    {
                                        MainActivity.mostrarMensaje(contexto,"Sitio existente","Ya existe" +
                                                " un sitio con este nombre");
                                    }
                                    else
                                    {
                                        databaseReference.setValue(nuevoSitio);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    MainActivity.mostrarMensaje(contexto,"Error",databaseError.getMessage());
                                    databaseError.toException().printStackTrace();
                                }
                            }
                    );

                    String key =databaseReference.push().getKey();
                    databaseReference.child(key).setValue(nuevoSitio);
                    finish();
                }
                else
                {
                    MainActivity.mostrarMensaje(this, "Error", "Parece que no has iniciado sesión. Intenta cerrar sesión e ingresar de nuevo");
                }

            }
        }
    }
}

        //TODO EDITAR SITIOS

            /*if(editar)
            {
                Sitio agregado = PlanIt.darInstancia().editarSitio(pos,nombre,barrio,direccion);
                Toast.makeText(this, "Tu sitio se editó", Toast.LENGTH_SHORT).show();
                setResult(pos);
                finish();
                Intent service = new Intent(this, PersitenciaService.class);
                service.putExtra("Requerimiento","EditarSitio");
                service.putExtra("Sitio", agregado);
                service.putExtra("Nombre",this.nombre);
                startService(service);
                agregado = null;
                this.nombre=null;
            }
            else
            {
                Sitio agregado = PlanIt.darInstancia().agregarSitio(nombre,barrio,direccion);
                Toast.makeText(this, "Tu sitio se agregó", Toast.LENGTH_SHORT).show();
                setResult(-1);
                finish();
                Intent intent = new Intent(this, PersitenciaService.class);
                intent.putExtra("Requerimiento","AgregarSitio");
                intent.putExtra("Sitio", agregado);
                startService(intent);
                agregado = null;
                System.out.println("Sitio agregado");
            }
            finish();*/

