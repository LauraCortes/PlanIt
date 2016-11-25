package com.example.laura.planit.Activities.Main;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.laura.planit.Fragments.TabFragment;
import com.example.laura.planit.Fragments.TabsFragmenPageAdapter;
import com.example.laura.planit.R;
import com.example.laura.planit.Sensores.DetectorAgitacion;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements DetectorAgitacion.OnShakeListener
{

    private boolean activityVisible;
    private long tiempoPrimerShake;
    private int cantidadShakes=0;
    private int id_Notificacion_llegada=420;
    private Context contexto;
    Vibrator vibrador;

    private SensorManager sensorMgr;
    private DetectorAgitacion detectorShake;


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

        contexto=this;

        //Crea el detectorShake de agitación
        sensorMgr = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if(sensorMgr!=null)
        {
            detectorShake = new DetectorAgitacion(this);
            sensorMgr.registerListener(detectorShake,sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        }
        else
        {
            System.out.println("No se pudo inicializar el sensor para detección de agitación");
        }

        //Crea el vibrador
        vibrador= (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);


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
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
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
    public void onBackPressed()
    {
        boolean ejecutado =false;
        for(android.support.v4.app.Fragment fragment : getSupportFragmentManager().getFragments())
        {
            TabFragment actual = (TabFragment)fragment;
            if(actual!= null && actual.isVisible() && actual.hayItemsSeleccionados())
            {
                actual.deseleccionar();
                ejecutado=true;
                break;
            }
        }
        if(!ejecutado)
        {
            super.onBackPressed();
        }
    }

    public void cerrarSesion()
    {
        SharedPreferences properties = this.getSharedPreferences(getString(R.string.properties), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = properties.edit();
        editor.putBoolean(getString(R.string.logueado),false);
        editor.putString(getString(R.string.usuario),"");
        editor.commit();
        finish();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public static void mostrarMensaje(Context contexto, String titutlo, String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle(titutlo);
        builder.setMessage(mensaje);
        builder.setCancelable(false);
        builder.setPositiveButton("OK",null);
        builder.show();
    }

    @Override
    public void onShake()
    {
        NotificationManager mNotifyMgr=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(cantidadShakes==0)
        {
            tiempoPrimerShake = System.currentTimeMillis();
            cantidadShakes++;
            if(activityVisible)
            {
                new mostrarShake().execute();
                Uri tono  = Uri.parse("android.resource://"+ this.getPackageName() + "/" + R.raw.notificacion_llegada);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), tono);
                r.play();
                vibrador.vibrate(500);
            }
            else
            {
                Drawable icon = ContextCompat.getDrawable(this,R.drawable.llegada_evento);
                icon.setBounds(0,0,25,25);
                Notification notificacion = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo_planit)
                        .setContentTitle("Nombre del evento")
                        .setContentText("Cuéntale a tus amigos que ya llegaste")
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setAutoCancel(false)
                        .addAction(R.drawable.check,"Ya llegué",null)
                        .addAction(R.drawable.close,"Posponer",null)
                        .setPriority(Notification.PRIORITY_MAX)
                        .build();

                //Tono de notificación
                notificacion.sound=Uri.parse("android.resource://"+ this.getPackageName() + "/" + R.raw.notificacion_llegada);
                //Vibración
                long[] vibrate = { 0, 100, 200, 100 };
                notificacion.vibrate=vibrate;
                // Builds the notification and issues it.
                mNotifyMgr.notify(id_Notificacion_llegada, notificacion);
            }
        }
        else
        {
            long actual = System.currentTimeMillis();
            long transcurrido = actual-tiempoPrimerShake;
            if(transcurrido/1000<8)
            {
                Uri tono  = Uri.parse("android.resource://"+ this.getPackageName() + "/" + R.raw.notificacion_confirmacion_llegada);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), tono);
                r.play();
                vibrador.vibrate(1000);
                Toast.makeText(contexto,"Le avisamos a tus amigos que llegaste",Toast.LENGTH_SHORT).show();
                cantidadShakes=0;
                mNotifyMgr.cancel(id_Notificacion_llegada);
                //Desactivar listener de shake
                sensorMgr.unregisterListener(detectorShake);
                //Avisar que ya llegó al evento
            }
            else
            {
                cantidadShakes=0;
                onShake();
            }
        }


    }



    private class mostrarShake extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object[] params) {
            try
            {
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



