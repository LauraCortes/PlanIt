package com.example.laura.planit.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.laura.planit.Logica.User;
import com.example.laura.planit.Persistencia.DBHandler;

import static android.content.Intent.getIntent;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String requerimiento = intent.getExtras().getString("Requerimiento");
        if(requerimiento.equals("Registrar"))
        {
            DBHandler db = (DBHandler) intent.getExtras().get("DB");
            User usuario = (User) intent.getExtras().get("Usuario");
            db.addUser(usuario);
            onDestroy();
        }
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
