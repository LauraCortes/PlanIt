package com.example.laura.planit.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;

import com.example.laura.planit.R;

/**
 * Created by Laura on 12/09/2016.
 */
public class RegistroActivity extends Activity{

    private String phoneNumb;

    private int confirmNumb;

    private boolean confirmado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        confirmado=false;
    }

    public void registrar(View view)
    {
        confirmNumb=(int)Math.random();
       EditText mEdit   = (EditText)findViewById(R.id.editTextPhoneNumber);
        SmsManager smsManager = SmsManager.getDefault();
        phoneNumb= mEdit.getText().toString();
        smsManager.sendTextMessage(phoneNumb, null, "Número de confirmación: "+confirmNumb, null, null);
    }

    public void confirmar(View view)
    {
        EditText mEdit   = (EditText)findViewById(R.id.editTextConfirmation);
        if(mEdit.getText().toString().equals(String.valueOf(confirmNumb)))
        {
            confirmado=true;
            finish();
        }
        else
        {

        }
        finish();
    }
}
