package com.example.laura.planit.Services;

import android.app.DownloadManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
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

    private ResumenEvento eventoMasCercano;
    Vibrator vibrador;
    Context contexto;
    private Location ubicacion;
    private JSONObject respuesta;
    private  String peticion;

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
            System.out.println("Llegó ubicación");

            final String celular = intent.getStringExtra(Constants.EXTRA_CELULAR);
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
                                        System.out.println("Delta tiempo con "+miEventoActual.getNombre()+" -> "+(deltaActual/(1000*60))+"minutos");
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
                                                        System.out.println("Delta tiempo con "+invitacionActual.getNombre()+" -> "+(deltaActual/(1000*60))+"minutos");
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
                                                    System.out.println("El evento mas cercano es "+eventoMasCercano.getNombre());
                                                    actualizarTiempoDistancia();
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



    private void actualizarTiempoDistancia()
    {
        //TODO
        String referencia =Constants.FIREBASE_URL+Constants.URL_EVENTOS+eventoMasCercano.getId_evento()+"/lugar";
        System.out.println("Ref->"+referencia);
        FirebaseDatabase.getInstance().getReferenceFromUrl(referencia).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            System.out.println("El lugar del evento está definido");

                            Sitio lugarEvento=dataSnapshot.getValue(Sitio.class);
                            String direccion = "https://maps.googleapis.com/maps/api/distancematrix/json?";
                            String origen ="origins="+ubicacion.getLatitude()+","+ubicacion.getLongitude();
                            String destino = "&destinations="+lugarEvento.getLatitud()+","+lugarEvento.getLongitud();
                            String key="&key=AIzaSyDmNOb3rqTWW0KqApkCunFvMwqdBbKZ5Dw";
                            String opciones="&language=es&traffic_model=pessimistic";

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

                System.out.println("JSON: " + jsonString);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }
    }
}

