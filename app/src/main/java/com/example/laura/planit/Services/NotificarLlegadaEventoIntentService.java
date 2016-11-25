package com.example.laura.planit.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Vibrator;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.laura.planit.Activities.Main.Constants;
import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Modelos.ResumenEvento;
import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Usuario on 10/11/2016.
 */

public class NotificarLlegadaEventoIntentService extends IntentService {

    private ResumenEvento eventoMasCercano;
    Vibrator vibrador;
    Context contexto;

    public NotificarLlegadaEventoIntentService()
    {
        super("");
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NotificarLlegadaEventoIntentService(String name) {
        super(name);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    @Override
    protected void onHandleIntent(Intent intent)
    {
        System.out.println("Solicitud de llegada a evento");
        contexto=this;

        vibrador= (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        //Encuentra el evento más cercano

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
                                                Uri tono  = Uri.parse("android.resource://"+ getApplicationContext().getPackageName() + "/" + R.raw.notificacion_confirmacion_llegada);
                                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), tono);
                                                r.play();
                                                vibrador.vibrate(1000);
                                                Toast.makeText(contexto,"Le avisamos a tus amigos que llegaste al evento "+eventoMasCercano.getNombre(),Toast.LENGTH_SHORT).show();
                                                NotificationManager mNotifyMgr=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                                mNotifyMgr.cancel(MainActivity.ID_NOTIFICACION_LLEGADA);
                                                //Avisar que ya llegó al evento

                                                FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL
                                                        +Constants.URL_PARTICIPANTES_EVENTO+eventoMasCercano.getId_evento()).child(celular).child("llego_evento").setValue(true);

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

