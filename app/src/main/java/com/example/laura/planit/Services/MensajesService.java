package com.example.laura.planit.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.example.laura.planit.Logica.Contacto;

import java.util.ArrayList;

/**
 * Created by Usuario on 18/09/2016.
 */
public class MensajesService extends Service
{
    public MensajesService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        String requerimiento = intent.getExtras().getString("Requerimiento");
        if(requerimiento!=null)
        {
            Toast.makeText(this,"Enviar sms",Toast.LENGTH_LONG).show();
            if(requerimiento.equals("EnviarALista"))
            {
                ArrayList<Contacto> contactos = (ArrayList<Contacto>)intent.getExtras().get("Contactos");
                String mensaje = intent.getExtras().getString("Msj");
                SmsManager smsManager = SmsManager.getDefault();
                for(Contacto contacto:contactos)
                {
                    smsManager.sendTextMessage(contacto.getNumeroTelefonico(), null, mensaje, null, null);
                }
                Toast.makeText(this,"Todos los contactos fueron invitados mediante SMS",Toast.LENGTH_LONG).show();
            }


        }
        onDestroy();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
