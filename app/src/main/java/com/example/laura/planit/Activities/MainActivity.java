package com.example.laura.planit.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.laura.planit.Activities.Contactos.ContactosActivity;
import com.example.laura.planit.Activities.Eventos.MisEventosActivity;
import com.example.laura.planit.Activities.Sitios.SitiosActivity;
import com.example.laura.planit.Logica.PlanIt;
import com.example.laura.planit.Persistencia.DBHandler;
import com.example.laura.planit.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    //Base de datos
    private DBHandler db ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBHandler(this);
        PlanIt mundo = PlanIt.darInstancia();
        mundo.setDB(db);
        mundo.inicializar();

        mundo=null;

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    public void Registrar(View view) {
        Intent i = new Intent(this, RegistroActivity.class);
        startActivity(i);
    }

    public void lanzarSitios(View view)
    {
        Intent i = new Intent(this, SitiosActivity.class);
        startActivity(i);
    }

    public void contactosEmergencia(View view)
    {
        Intent i = new Intent(this, ContactosActivity.class);
        startActivity(i);
    }

    public void lanzarEventos(View view)
    {
        startActivity(new Intent(this, MisEventosActivity.class));
    }
}
