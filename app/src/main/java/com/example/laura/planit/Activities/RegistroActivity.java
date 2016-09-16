package com.example.laura.planit.Activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.laura.planit.Logica.User;
import com.example.laura.planit.Persistencia.DBHandler;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.MyService;

import java.io.Serializable;

/**
 * Created by Laura on 12/09/2016.
 */
public class RegistroActivity extends Activity{

    private User usuario;

    private int confirmNumb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    public void registrar(View view)
    {
        confirmNumb=(int)Math.random();
        EditText mEdit   = (EditText)findViewById(R.id.editTextPhoneNumber);
        EditText mEdit2   = (EditText)findViewById(R.id.editTextName);
        SmsManager smsManager = SmsManager.getDefault();
        usuario = new User (Integer.parseInt(mEdit.getText().toString()),mEdit2.getText().toString() );
        smsManager.sendTextMessage(mEdit.getText().toString(), null, "Número de confirmación: "+confirmNumb, null, null);
    }

    public void confirmar(View view) {
        EditText mEdit = (EditText) findViewById(R.id.editTextConfirmation);
        if (mEdit.getText().toString().equals(String.valueOf(confirmNumb))) {
            Intent intent = new Intent(this, MyService.class);
            intent.putExtra("Requerimiento","Registrar");
            intent.putExtra("Usuario", usuario);
            DBHandler db= (DBHandler) getIntent().getExtras().get("DB");
            intent.putExtra("DB",db);
            startService(intent);
            usuario = null;
            finish();
        } else {
            mEdit.setText("Número equivocado");
        }
    }
}
