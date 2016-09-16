package com.example.laura.planit.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    public void Registrar(View view) {
        Intent i = new Intent(this, RegistroActivity.class);
        i.putExtra("DB",db);
        startActivity(i);
    }

    public void lanzarSitios(View view)
    {
        Intent i = new Intent(this, SitiosActivity.class);
        startActivity(i);
    }
}
