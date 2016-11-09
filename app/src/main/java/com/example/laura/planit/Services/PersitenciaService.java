package com.example.laura.planit.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.example.laura.planit.Modelos.Contacto;
import com.example.laura.planit.Modelos.Evento;
import com.example.laura.planit.Modelos.MedioTransporte;
import com.example.laura.planit.Modelos.PlanIt;
import com.example.laura.planit.Modelos.Sitio;
import com.example.laura.planit.Modelos.Usuario;
import com.example.laura.planit.Persistencia.DBHandler;

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
        if(requerimiento!=null)
        {

            if(requerimiento.equals("Registrar")) {
                Usuario usuario = (Usuario) intent.getExtras().get("Usuario");
                db.addUser(usuario);
            }
            else if (requerimiento.equals("AgregarContacto"))
            {
                Contacto contacto = (Contacto) intent.getExtras().get("Contacto");
                try
                {
                    db.agregarContacto(contacto);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(this, "Error persistiendo datos: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                contacto=null;
            }
            else if (requerimiento.equals("AgregarSitio"))
            {
                Sitio sitio = (Sitio) intent.getExtras().get("Sitio");
                try
                {
                    db.agregarSitio(sitio);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(this, "Error persistiendo datos: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                sitio=null;
            }
            else if (requerimiento.equals("EditarSitio"))
            {
                Sitio sitio = (Sitio) intent.getExtras().get("Sitio");
                db.editarSitio(intent.getExtras().getString("Nombre"),sitio);
                sitio=null;
            }
            else if (requerimiento.equals("MarcarContacto"))
            {
                Contacto contacto = (Contacto) intent.getExtras().get("Contacto");
                int marcado = intent.getExtras().getInt("Favoritos");
                db.marcarContacto(contacto,marcado);
                contacto=null;
            }
            else if (requerimiento.equals("EliminarSitio"))
            {
                db.eliminarSitio(intent.getExtras().getString("Nombre"));
            }
            else if (requerimiento.equals("AgregarContacto"))
            {
                Contacto contacto = (Contacto) intent.getExtras().get("Contacto");
                try
                {
                    db.agregarContacto(contacto);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(this, "Error persistiendo datos: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                contacto=null;
            }
            else if (requerimiento.equals("AgregarSitio"))
            {
                Sitio sitio = (Sitio) intent.getExtras().get("Sitio");
                try
                {
                    db.agregarSitio(sitio);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(this, "Error persistiendo datos: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                sitio=null;
            }
            else if (requerimiento.equals("EditarSitio"))
            {
                Sitio sitio = (Sitio) intent.getExtras().get("Sitio");
                db.editarSitio(intent.getExtras().getString("Nombre"),sitio);
                sitio=null;
            }
            else if (requerimiento.equals("EliminarSitio"))
            {
                db.eliminarSitio(intent.getExtras().getString("Nombre"));
            }
            else if (requerimiento.equals("EliminarContacto"))
            {
                db.eliminarContacto(intent.getExtras().getString("Telefono"));
            }
            else if(requerimiento.equals("AgregarEvento"))
            {
                db.agregarEvento((Evento)intent.getExtras().get("Evento"));
                Toast.makeText(this, db.darEventos().size()+" eventos!", Toast.LENGTH_LONG);

            }
            else if(requerimiento.equals("EliminarEvento"))
            {
                db.eliminarEvento(intent.getExtras().getString("Nombre"));
            }
            else if (requerimiento.equals("AgregarRegresoSitio"))
            {
                db.agregarTransporteAEvento((MedioTransporte)intent.getExtras().get("Medio"),intent.getExtras().getString("Nombre"));
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
