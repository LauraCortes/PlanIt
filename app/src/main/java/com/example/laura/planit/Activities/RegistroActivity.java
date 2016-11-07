package com.example.laura.planit.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.laura.planit.Logica.Usuario;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.PersitenciaService;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Laura on 12/09/2016.
 */
public class RegistroActivity extends AppCompatActivity
{

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=1;
    private Usuario usuario;

    private int confirmNumb;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setSupportActionBar(null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
    }

    public void registrar(View view)
    {
        confirmNumb=(int)Math.random();
        EditText mEdit   = (EditText)findViewById(R.id.txtCelularLogin);
        EditText mEdit2   = (EditText)findViewById(R.id.txtPasswordLogin);

        usuario = new Usuario(mEdit.getText().toString(),mEdit2.getText().toString() );

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                showSnackBar();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);

            }
        }
        else
        {
            /*
            EditText mEdit3 = (EditText) findViewById(R.id.editTextConfirmation);
            mEdit3.setHint(String.valueOf(confirmNumb));
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(usuario.getNumeroTelefonico(), null, "Número de confirmación: "+confirmNumb, null, null);
*/
        }
    }

    private void showSnackBar() {
        Snackbar.make(findViewById(R.id.relativeLayout),"Activar envío sms",Snackbar.LENGTH_LONG)
                .setAction("Settings", new View.OnClickListener() {
                    @Override public void onClick(View view){
                        openSettings();
                    }}).show();
    }

    public void openSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   /* EditText mEdit = (EditText) findViewById(R.id.editTextConfirmation);
                    mEdit.setHint(confirmNumb);
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(usuario.getNumeroTelefonico(), null, "Número de confirmación: "+confirmNumb, null, null);*/
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    showSnackBar();
                   // finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void confirmar(View view) {
       /* EditText mEdit = (EditText) findViewById(R.id.editTextConfirmation);
        if (mEdit.getText().toString().equals(String.valueOf(confirmNumb))) {
            Intent intent = new Intent(this, PersitenciaService.class);
            intent.putExtra("Requerimiento","Registrar");
            intent.putExtra("Usuario", usuario);
            startService(intent);
            usuario = null;
            finish();
        } else {
            mEdit.setHint("Número equivocado");
        }*/
    }
}
