package com.example.laura.planit.Activities.Sitios;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Modelos.PlanIt;
import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.PersitenciaService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.BreakIterator;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.example.laura.planit.R.id.map;

/**
 * Created by Laura on 12/09/2016.
 */
public class AgregarSitioActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{

    private EditText txtNombre;
    private TextView txtDireccion;
    private boolean editar;
    private int pos;
    private String nombre;
    Context contexto;
    public GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private int mLatitude;
    private int  mLongitude;
    private GoogleMap mapa;
    private Marker defaultMarker;

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
        contexto = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_sitios);
        txtNombre = (EditText) findViewById(R.id.txtNombreSitioFavorito);
        txtDireccion = (TextView) findViewById(R.id.txtDireccionFavorito);


        //ActionBar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_agregar_sitio));
        getSupportActionBar().setTitle("Agregar sitio");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.atras_icon);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        Intent intent = getIntent();
        editar = intent.getExtras().getBoolean("editar");
        if (editar) {
            Sitio sitio = (Sitio) intent.getSerializableExtra("sitio");
            if (sitio != null) {
                txtNombre.setText(sitio.getNombre());
                txtDireccion.setText(sitio.getDirección());
                txtDireccion.setEnabled(false);
                getSupportActionBar().setTitle("Edición de sitio");
            } else {
                finish();
            }

        } else {

        }
        intent = null;
    }

    protected void onStart() {
        //Mapa
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        //Api para consulta de la ubicación actual
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
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
            } else {
                txtDireccion.setError(null);
                //public Sitio(int latitud, int longitud, String nombre, String dirección) {
                final Sitio nuevoSitio = new Sitio();
                nuevoSitio.setDirección(direccion);
                nuevoSitio.setNombre(nombre);
                LatLng posicionActual=defaultMarker.getPosition();
                nuevoSitio.setLatitud(posicionActual.latitude);
                nuevoSitio.setLongitud(posicionActual.longitude);

                SharedPreferences properties = this.getSharedPreferences(getString(R.string.properties), Context.MODE_PRIVATE);
                if (properties.getBoolean(getString(R.string.logueado), false)) {
                    String celular = properties.getString(getString(R.string.usuario), "desconocido");
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference databaseReference = database.getReferenceFromUrl(PlanIt.FIREBASE_URL).child(nuevoSitio.darRutaElemento(celular));
                    databaseReference.addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        MainActivity.mostrarMensaje(contexto, "Sitio existente", "Ya existe" +
                                                " un sitio con este nombre");
                                    } else {
                                        databaseReference.setValue(nuevoSitio);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    MainActivity.mostrarMensaje(contexto, "Error", databaseError.getMessage());
                                    databaseError.toException().printStackTrace();
                                }
                            }
                    );

                    String key = databaseReference.push().getKey();
                    databaseReference.child(key).setValue(nuevoSitio);
                    finish();
                } else {
                    MainActivity.mostrarMensaje(this, "Error", "Parece que no has iniciado sesión. Intenta cerrar sesión e ingresar de nuevo");
                }

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapa=googleMap;

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mapa!=null)
        {

            String nombreActual=String.valueOf(txtNombre.getText()).trim();
            String msj = mLastLocation!=null?"Ubicación actual":
                    (nombreActual.isEmpty()?"Nuevo sitio":nombreActual);
            LatLng actual = mLastLocation!=null?new LatLng(mLastLocation.getLatitude(),
                    mLastLocation.getLongitude()):new LatLng(4.6027453,-74.0654616);
            defaultMarker=mapa.addMarker(new MarkerOptions()
                    .draggable(true)
                    .position(actual)
                    .title(msj));
            mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(actual,18));
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

