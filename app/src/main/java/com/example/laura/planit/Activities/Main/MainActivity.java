package com.example.laura.planit.Activities.Main;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.laura.planit.Activities.Eventos.DetallesEventoActivity;
import com.example.laura.planit.Activities.Eventos.RegresoRecyclerViewAdapter;
import com.example.laura.planit.Fragments.TabFragment;
import com.example.laura.planit.Fragments.TabsFragmenPageAdapter;
import com.example.laura.planit.Modelos.Movimiento;
import com.example.laura.planit.Modelos.ParticipanteEvento;
import com.example.laura.planit.Modelos.Regreso;
import com.example.laura.planit.Modelos.ResumenEvento;
import com.example.laura.planit.R;
import com.example.laura.planit.Sensores.DetectorAgitacion;
import com.example.laura.planit.Services.ActualizarTiempoDistanciaEventoIntentService;
import com.example.laura.planit.Services.NotificarLlegadaEventoIntentService;
import com.example.laura.planit.Services.ObtenerDireccionesIntentService;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements DetectorAgitacion.OnShakeListener {

    private boolean activityVisible;
    private long tiempoPrimerShake;
    private int cantidadShakes = 0;
    public final static int ID_NOTIFICACION_LLEGADA = 420;
    private Context contexto;

    Vibrator vibrador;

    private SensorManager sensorMgr;
    private DetectorAgitacion detectorShake;

    private String celular;

    private LocationManager locationManager = null;

    private Button btnGrabar;
    private ResumenEvento eventoMasCercano;
    private ArrayList<ParticipanteEvento> participantesEvento;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btn_cerrar_sesion_menu:
                cerrarSesion();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contexto = this;

        //Crea el detectorShake de agitación
        sensorMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorMgr != null) {
            detectorShake = new DetectorAgitacion(this);
            sensorMgr.registerListener(detectorShake, sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        } else {
            System.out.println("No se pudo inicializar el sensor para detección de agitación");
        }

        //Crea el vibrador
        vibrador = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        //Obtiene el número del celular
        SharedPreferences properties = this.getSharedPreferences(getString(R.string.properties), Context.MODE_PRIVATE);
        if (properties.getBoolean(getString(R.string.logueado), false)) {
            celular = properties.getString(getString(R.string.usuario), "desconocido");
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 120000, 50, new MyLocationListener());
                }

            }
        } else {
            finish();
        }


        //Crear el toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_principal));

        // Preparar las pestañas
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("MIS EVENTOS").setIcon(R.drawable.calendar_tab));
        tabs.addTab(tabs.newTab().setText("INVITACIONES").setIcon(R.drawable.calendar_tab));
        //tabs.addTab(tabs.newTab().setText("MOVIMIENTOS").setIcon(R.drawable.movimientos_tab));

        tabs.addTab(tabs.newTab().setText("SITIOS").setIcon(R.drawable.sitios_favoritos_tab));
        tabs.addTab(tabs.newTab().setText("AMIGOS").setIcon(R.drawable.amigos_favoritos_tab));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);

        // Setear adaptador al viewpager.
        final ViewPager viewPagerTabs = (ViewPager) findViewById(R.id.pager);
        final TabsFragmenPageAdapter adapter = new TabsFragmenPageAdapter(getSupportFragmentManager(), tabs.getTabCount());
        viewPagerTabs.setAdapter(adapter);
        viewPagerTabs.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerTabs.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Boton de grabar
        btnGrabar=(Button) findViewById(R.id.btn_microfono);
        btnGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, 1);
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    if(text.get(0).equals("en cuanto llegan")||text.get(0).equals("se demoran")||text.get(0).equals("ya vienen")
                            ||text.get(0).equals("ya llegaron"))
                    {
                        final String celular = getIntent().getStringExtra(Constants.EXTRA_CELULAR);
                        if(celular!=null)
                        {
                            Query misEventosOrdenadosFecha = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL+Constants.URL_MIS_EVENTOS).
                                    child(celular).orderByChild("fecha");
                            misEventosOrdenadosFecha.addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot)
                                        {
                                            final ResumenEvento[] encontrado = {null};
                                            final long fechaActual = System.currentTimeMillis();
                                            final long[] deltaCercano = {86400000}; // 24 horas

                                            GenericTypeIndicator<HashMap<String,ResumenEvento>> t = new GenericTypeIndicator<HashMap<String, ResumenEvento>>(){};
                                            HashMap<String, ResumenEvento> map =dataSnapshot.getValue(t);
                                            if(map!=null)
                                            {
                                                for(ResumenEvento miEventoActual : map.values())
                                                {
                                                    long deltaActual = Math.abs(miEventoActual.fecha-fechaActual);
                                                    if(deltaActual< deltaCercano[0])
                                                    {
                                                        deltaCercano[0] =deltaActual;
                                                        encontrado[0] =miEventoActual;
                                                        eventoMasCercano=miEventoActual;
                                                    }
                                                }
                                            }

                                            FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL+Constants.URL_INVITACIONES_EVENTO).
                                                    child(celular).orderByChild("fecha").addListenerForSingleValueEvent(
                                                    new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                            GenericTypeIndicator<HashMap<String,ResumenEvento>> t = new GenericTypeIndicator<HashMap<String, ResumenEvento>>(){};
                                                            HashMap<String, ResumenEvento> map =dataSnapshot.getValue(t);
                                                            if(map!=null)
                                                            {
                                                                for(ResumenEvento invitacionActual : map.values())
                                                                {
                                                                    long deltaActual = Math.abs(invitacionActual.fecha-fechaActual);
                                                                    if(deltaActual< deltaCercano[0])
                                                                    {
                                                                        deltaCercano[0] =deltaActual;
                                                                        encontrado[0] =invitacionActual;
                                                                        eventoMasCercano=invitacionActual;
                                                                    }
                                                                }
                                                            }

                                                            if(eventoMasCercano!=null)
                                                            {
                                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                                final DatabaseReference databaseReference = database.getReferenceFromUrl(Constants.FIREBASE_URL).child("participantes_evento/" + eventoMasCercano.getId_evento());
                                                                databaseReference.keepSynced(true);
                                                                databaseReference.addValueEventListener(
                                                                        new ValueEventListener() {
                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot)
                                                                            {
                                                                                GenericTypeIndicator<HashMap<String,ParticipanteEvento>> t = new GenericTypeIndicator<HashMap<String, ParticipanteEvento>>(){};
                                                                                HashMap<String, ParticipanteEvento> map =dataSnapshot.getValue(t);
                                                                                if(map!=null)
                                                                                {
                                                                                  participantesEvento = new ArrayList(map.values());

                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(DatabaseError databaseError) {

                                                                            }
                                                                        }
                                                                );
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    }
                                            );

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }
                            );
                        }
                    }
                }
                break;
            }

        }
    }

    public void mostrarDialogoParticipantesEvento()
    {
        if(participantesEvento!=null)
        {
            String[] arrayParticipantes = new String[participantesEvento.size()];
            int i=0;
            for(ParticipanteEvento partActual:participantesEvento)
            {
                arrayParticipantes[i]=partActual.toString();
                i++;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
            builder.setTitle(eventoMasCercano.getNombre()+" - Participantes");
            builder.setCancelable(true);
            builder.setPositiveButton("ACEPTAR",null);
            builder.setItems(arrayParticipantes,null);
            builder.show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        activityVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityVisible = false;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        boolean ejecutado = false;
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            TabFragment actual = (TabFragment) fragment;
            if (actual != null && actual.isVisible() && actual.hayItemsSeleccionados()) {
                actual.deseleccionar();
                ejecutado = true;
                break;
            }
        }
        if (!ejecutado) {
            super.onBackPressed();
        }
    }

    public void cerrarSesion() {
        SharedPreferences properties = this.getSharedPreferences(getString(R.string.properties), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = properties.edit();
        editor.putBoolean(getString(R.string.logueado), false);
        editor.putString(getString(R.string.usuario), "");
        editor.commit();
        finish();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public static void mostrarMensaje(Context contexto, String titutlo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle(titutlo);
        builder.setMessage(mensaje);
        builder.setCancelable(false);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    @Override
    public void onShake() {
        Intent intentNotificarLLegada = new Intent(contexto, NotificarLlegadaEventoIntentService.class);
        intentNotificarLLegada.putExtra(Constants.EXTRA_CELULAR, celular);

        if (cantidadShakes == 0) {
            tiempoPrimerShake = System.currentTimeMillis();
            cantidadShakes++;
            if (activityVisible) {
                Uri tono = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.notificacion_llegada);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), tono);
                r.play();
                vibrador.vibrate(500);
                new mostrarShake().execute();
            } else {
                PendingIntent pendingIntent = PendingIntent.getService(contexto, 0, intentNotificarLLegada, 0);

                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Drawable icon = ContextCompat.getDrawable(this, R.drawable.llegada_evento);
                icon.setBounds(0, 0, 25, 25);
                Notification notificacion = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo_planit)
                        .setContentTitle("Llegada a evento")
                        .setContentText("Cuéntale a tus amigos que ya llegaste")
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setAutoCancel(false)
                        .addAction(R.drawable.check, "Ya llegué", pendingIntent)
                        .setPriority(Notification.PRIORITY_MAX)
                        .build();

                //Tono de notificación
                notificacion.sound = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.notificacion_llegada);
                //Vibración
                long[] vibrate = {0, 100, 200, 100};
                notificacion.vibrate = vibrate;
                // Builds the notification and issues it.
                mNotifyMgr.notify(ID_NOTIFICACION_LLEGADA, notificacion);
            }
        } else {
            long actual = System.currentTimeMillis();
            long transcurrido = actual - tiempoPrimerShake;
            if (transcurrido / 1000 < 8) {
                cantidadShakes = 0;
                //Desactivar listener de shake
                //sensorMgr.unregisterListener(detectorShake);
                //Enviar el intent
                contexto.startService(intentNotificarLLegada);

            } else {
                cantidadShakes = 0;
                onShake();
            }
        }


    }



    private class MyLocationListener implements LocationListener {

        public MyLocationListener()
        {

        }
        @Override
        public void onLocationChanged(Location location) {
            DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL);
            if (location != null) {
                db.child(Constants.URL_USUARIOS).child(celular).child("latitud_actual").setValue(location.getLatitude());
                db.child(Constants.URL_USUARIOS).child(celular).child("longitud_actual").setValue(location.getLongitude());
                Intent actualizar = new Intent(contexto,ActualizarTiempoDistanciaEventoIntentService.class);
                actualizar.putExtra(Constants.EXTRA_UBICACION,location);
                actualizar.putExtra(Constants.EXTRA_CELULAR,celular);
                contexto.startService(actualizar);
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }





    private class mostrarShake extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Thread.sleep(3000);
                return null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            findViewById(R.id.frame_shake_detected).setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.frame_shake_detected).setVisibility(View.VISIBLE);
        }
    }


}



