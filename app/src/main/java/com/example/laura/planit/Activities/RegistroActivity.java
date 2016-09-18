package com.example.laura.planit.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;

import com.example.laura.planit.Logica.Usuario;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.PersitenciaService;

/**
 * Created by Laura on 12/09/2016.
 */
public class RegistroActivity extends Activity{

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=1;
    private Usuario usuario;

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

        usuario = new Usuario(mEdit.getText().toString(),mEdit2.getText().toString() );
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.SEND_SMS},
                MY_PERMISSIONS_REQUEST_SEND_SMS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(usuario.getNumeroTelefonico(), null, "Número de confirmación: "+confirmNumb, null, null);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void confirmar(View view) {
        EditText mEdit = (EditText) findViewById(R.id.editTextConfirmation);
        if (mEdit.getText().toString().equals(String.valueOf(confirmNumb))) {
            Intent intent = new Intent(this, PersitenciaService.class);
            intent.putExtra("Requerimiento","Registrar");
            intent.putExtra("Usuario", usuario);
            startService(intent);
            usuario = null;
            finish();
        } else {
            mEdit.setHint("Número equivocado");
        }
    }
}
