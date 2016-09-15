package com.example.laura.planit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Laura on 12/09/2016.
 */
public class SitiosActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sitios);

    }

    public void agregarSitio(View view)
    {
        Intent i = new Intent(this, AgregarSitioActivity.class);
        startActivity(i);
    }


}
