package com.example.laura.planit.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.Logica.Sitio;
import com.example.laura.planit.Logica.User;
import com.example.laura.planit.Persistencia.DBHandler;

import static android.content.Intent.getIntent;

public class PersitenciaService extends Service {
    public PersitenciaService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        DBHandler db = PlanIt.darInstancia().getDb();
        String requerimiento = intent.getExtras().getString("Requerimiento");
        if(requerimiento.equals("Registrar"))
        {

            User usuario = (User) intent.getExtras().get("Usuario");
            db.addUser(usuario);

        }
        else if (requerimiento.equals("AgregarSitio"))
        {

            Toast.makeText(this, ("DB nula->"+(db==null)), Toast.LENGTH_SHORT).show();

            Sitio sitio = (Sitio) intent.getExtras().get("Sitio");
            try
            {

                db.agregarSitio(sitio);
                Toast.makeText(this, "Sitio persistido en la DB", Toast.LENGTH_SHORT).show();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                Toast.makeText(this, "Error persistiendo datos", Toast.LENGTH_SHORT).show();
            }
            sitio=null;
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
