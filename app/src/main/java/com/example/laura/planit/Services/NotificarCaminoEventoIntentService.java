package com.example.laura.planit.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.Toast;

import com.example.laura.planit.Activities.Main.Constants;
import com.example.laura.planit.Activities.Main.MainActivity;
import com.example.laura.planit.Modelos.Movimiento;
import com.example.laura.planit.Modelos.ResumenEvento;
import com.example.laura.planit.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by Usuario on 10/11/2016.
 */

public class NotificarCaminoEventoIntentService extends IntentService {

    private ResumenEvento eventoMasCercano;
    Vibrator vibrador;
    Context contexto;

    public NotificarCaminoEventoIntentService()
    {
        super("");
    }
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NotificarCaminoEventoIntentService(String name) {
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
        String id_evento = intent.getStringExtra(Constants.EXTRA_ID_EVENTO);
        if(celular!=null && id_evento!=null)
        {
            final DatabaseReference refCaminoEvento = FirebaseDatabase.getInstance().getReferenceFromUrl(Constants.FIREBASE_URL+Constants.URL_PARTICIPANTES_EVENTO)
                    .child(id_evento).child(celular).child("camino_evento");
            refCaminoEvento.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists() && !dataSnapshot.getValue(Boolean.class))
                            {
                                //No va camino al evento
                                Uri tono  = Uri.parse("android.resource://"+ getApplicationContext().getPackageName() + "/" + R.raw.notificacion_confirmacion_llegada);
                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), tono);
                                r.play();
                                vibrador.vibrate(1000);
                                Toast.makeText(contexto,"Le avisamos a tus amigos que vas camino al evento",Toast.LENGTH_SHORT).show();
                                NotificationManager mNotifyMgr=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                mNotifyMgr.cancel(MainActivity.ID_NOTIFICACION_LLEGADA);
                                //Avisar que ya llegó al evento
                                refCaminoEvento.setValue(true);
                            }
                            else
                            {
                                //El evento existe o ya dijo que iba camino al evento
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );
        }
    }
}

