package com.example.laura.planit.Services;

import android.app.DownloadManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.laura.planit.Activities.Main.Constants;
import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Modelos.ResumenEvento;
import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.R;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

/**
 * Created by Usuario on 10/11/2016.
 */

public class ActualizarTiempoDistanciaEventoIntentService extends IntentService {

    private ResumenEvento eventoMasCercano=null;
    private ResumenEvento eventoProximoCercano=null;
    private long deltaPositivoCercano=3600000;
    Vibrator vibrador;
    Context contexto;
    private Location ubicacion;
    private JSONObject respuesta;
    private  String peticion;
    private String celular;

    private boolean notificacionLanzada=false;
    private boolean notificacionProximoEvento =false;

    public ActualizarTiempoDistanciaEventoIntentService()
    {
        super("");
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ActualizarTiempoDistanciaEventoIntentService(String name) {
        super(name);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    @Override
    protected void onHandleIntent(Intent intent)
    {
        contexto=this;
        System.out.println("lanzado evento de calculo distancias");

        ubicacion = (Location) intent.getExtras().get(Constants.EXTRA_UBICACION);
        if(ubicacion!=null)
        {
            //Encuentra el evento más cercano

            celular = intent.getStringExtra(Constants.EXTRA_CELULAR);
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
                                final long[] deltaCercano = {3600000}; // 1 hora

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
                                        long deltaPositivo = miEventoActual.fecha-fechaActual;
                                        System.out.println("* "+deltaActual);
                                        System.out.println("Delta tiempo con "+miEventoActual.getNombre()+" -> "+(deltaPositivo/(1000*60))+"minutos  vs el menor existente "+deltaPositivoCercano);
                                        if(deltaPositivo>=0 && deltaPositivo<=deltaPositivoCercano)
                                        {
                                            eventoProximoCercano=miEventoActual;
                                            deltaPositivoCercano=deltaPositivo;
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
                                                        long deltaPositivo = invitacionActual.fecha-fechaActual;
                                                        System.out.println("* "+deltaActual);
                                                        System.out.println("Delta tiempo con "+invitacionActual.getNombre()+" -> "+(deltaPositivo/(1000*60))+"minutos  vs el menor existente "+deltaPositivoCercano);
                                                        if(deltaPositivo>=0 && deltaPositivo<=deltaPositivoCercano)
                                                        {
                                                            eventoProximoCercano=invitacionActual;
                                                            deltaPositivoCercano=deltaPositivo;
                                                        }
                                                    }
                                                }

                                                if(eventoMasCercano!=null)
                                                {
                                                    System.out.println("El evento mas cercano es "+eventoMasCercano.getNombre());
                                                    actualizarTiempoDistancia();
                                                }
                                                mostrarNotificacionProximoEvento();

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


    private void mostrarNotificacionProximoEvento()
    {
        System.out.println("Solicitó mostrar la notificación");
        if(eventoProximoCercano!=null)
        {
            System.out.println("El evento "+eventoProximoCercano.getNombre()+" está próximo");
            FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL+Constants.URL_PARTICIPANTES_EVENTO)
                    .child(eventoProximoCercano.getId_evento()).child(celular).child("camino_evento").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists() && !dataSnapshot.getValue(Boolean.class))
                            {
                                //No va en camino-> mostrar notificación
                                if(!notificacionProximoEvento)
                                {


                                    notificacionProximoEvento=true;
                                    Intent intentNotificarEvento = new Intent(contexto, NotificarCaminoEventoIntentService.class);
                                    intentNotificarEvento.putExtra(Constants.EXTRA_CELULAR, celular);
                                    intentNotificarEvento.putExtra(Constants.EXTRA_ID_EVENTO,eventoProximoCercano.getId_evento());
                                    PendingIntent pendingIntent = PendingIntent.getService(contexto, 1, intentNotificarEvento, PendingIntent.FLAG_UPDATE_CURRENT);
                                    NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    Drawable icon = ContextCompat.getDrawable(contexto, R.drawable.llegada_evento);
                                    icon.setBounds(0, 0, 25, 25);
                                    Notification notificacion = new NotificationCompat.Builder(contexto)
                                            .setSmallIcon(R.drawable.logo_planit)
                                            .setContentTitle("Se acerca "+eventoProximoCercano.getNombre())
                                            .setContentText("Cuéntale a tus amigos que ya vas en camino")
                                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                            .setAutoCancel(false)
                                            .addAction(R.drawable.check, "Voy en camino", pendingIntent)
                                            .setPriority(Notification.PRIORITY_MAX)
                                            .build();

                                    //Tono de notificación
                                    notificacion.sound = Uri.parse("android.resource://" + contexto.getPackageName() + "/" + R.raw.notificacion_llegada);
                                    //Vibración
                                    long[] vibrate = {0, 100, 200, 100};
                                    notificacion.vibrate = vibrate;
                                    // Builds the notification and issues it.
                                    mNotifyMgr.notify(MainActivity.ID_NOTIFICACION_LLEGADA, notificacion);
                                }
                            }
                            else
                            {
                                //Va en camino-> No hacer nada
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );

        }
    }



    private void actualizarTiempoDistancia()
    {
        String referencia =Constants.FIREBASE_URL+Constants.URL_EVENTOS+eventoMasCercano.getId_evento()+"/lugar";
        System.out.println("Ref->"+referencia);
        FirebaseDatabase.getInstance().getReferenceFromUrl(referencia).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            long now = System.currentTimeMillis();

                            Sitio lugarEvento=dataSnapshot.getValue(Sitio.class);
                            String direccion = "https://maps.googleapis.com/maps/api/distancematrix/json?";
                            String origen ="origins="+ubicacion.getLatitude()+","+ubicacion.getLongitude();
                            String destino = "&destinations="+lugarEvento.getLatitud()+","+lugarEvento.getLongitud();
                            String key="&key=AIzaSyDmNOb3rqTWW0KqApkCunFvMwqdBbKZ5Dw";
                            String opciones="&language=es&traffic_model=pessimistic&departure_time="+now;

                            peticion = direccion+origen+destino+key+opciones;
                            new Request().execute();



                        }
                        else
                        {
                            System.out.println("No se encontró el lugar del evento");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }


    private class Request extends AsyncTask
    {

        @Override
        protected Object doInBackground(Object[] params) {

            HttpURLConnection urlConnection = null;

            java.net.URL url = null;
            try
            {
                url = new URL(peticion);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setDoOutput(true);
                urlConnection.connect();
                BufferedReader br=new BufferedReader(new InputStreamReader(url.openStream()));
                char[] buffer = new char[1024];
                String jsonString = new String();
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();

                jsonString = sb.toString();

                respuesta=new JSONObject(jsonString);
                if(respuesta!=null)
                {
                    String destino = (String) respuesta.getJSONArray("destination_addresses").get(0);
                    String origen = (String) respuesta.getJSONArray("origin_addresses").get(0);
                    JSONObject elements = respuesta.getJSONArray("rows").getJSONObject(0);
                    JSONObject detalles = elements.getJSONArray("elements").getJSONObject(0);
                    String distancia =detalles.getJSONObject("distance").getString("text");
                    String duracion = detalles.getJSONObject("duration").getString("text");
                    int distanciaMetros = detalles.getJSONObject("distance").getInt("value");
                    System.out.println("RESULTADO->"+destino+"-"+origen+": "+distancia+", "+duracion);

                    HashMap<String,Object> hijos = new HashMap<>();
                    hijos.put("distancia",distancia);
                    hijos.put("tiempo_llegada",duracion);
                    hijos.put("distancia_metros",distanciaMetros);

                    FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL
                            +Constants.URL_PARTICIPANTES_EVENTO+eventoMasCercano.getId_evento()).child(celular).updateChildren(hijos);

                    if(distanciaMetros<=100 && !notificacionLanzada)
                    {
                        notificacionLanzada=true;
                        Intent intentNotificarLLegada = new Intent(contexto, NotificarLlegadaEventoIntentService.class);
                        intentNotificarLLegada.putExtra(Constants.EXTRA_CELULAR, celular);
                        PendingIntent pendingIntent = PendingIntent.getService(contexto, 1, intentNotificarLLegada, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        Drawable icon = ContextCompat.getDrawable(contexto, R.drawable.llegada_evento);
                        icon.setBounds(0, 0, 25, 25);
                        Notification notificacion = new NotificationCompat.Builder(contexto)
                                .setSmallIcon(R.drawable.logo_planit)
                                .setContentTitle("Llegada a evento")
                                .setContentText("Cuéntale a tus amigos que ya llegaste")
                                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                                .setAutoCancel(false)
                                .addAction(R.drawable.check, "Ya llegué", pendingIntent)
                                .setPriority(Notification.PRIORITY_MAX)
                                .build();

                        //Tono de notificación
                        notificacion.sound = Uri.parse("android.resource://" + contexto.getPackageName() + "/" + R.raw.notificacion_llegada);
                        //Vibración
                        long[] vibrate = {0, 100, 200, 100};
                        notificacion.vibrate = vibrate;
                        // Builds the notification and issues it.
                        mNotifyMgr.notify(MainActivity.ID_NOTIFICACION_LLEGADA, notificacion);
                    }
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }
    }
}

