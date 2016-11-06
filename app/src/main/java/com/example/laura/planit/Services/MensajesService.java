package com.example.laura.planit.Services;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.laura.planit.Logica.Contacto;
import com.example.laura.planit.R;

import java.util.ArrayList;

/**
 * Created by Usuario on 18/09/2016.
 */
public class MensajesService extends Service
{
    private ArrayList<Contacto> contactos;
    private String msj;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=1;
    public MensajesService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        String requerimiento = intent.getExtras().getString("Requerimiento");
        if(requerimiento!=null)
        {
            if(requerimiento.equals("EnviarALista"))
            {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getApplicationContext(),
                            Manifest.permission.SEND_SMS)) {
                        showSnackBar();
                    } else {
                        ActivityCompat.requestPermissions((Activity)getApplicationContext(),
                                new String[]{Manifest.permission.SEND_SMS},
                                MY_PERMISSIONS_REQUEST_SEND_SMS);

                    }
                }
                else
                {
                    contactos = (ArrayList<Contacto>)intent.getExtras().get("Contactos");
                    msj = intent.getExtras().getString("Msj");
                    SmsManager smsManager = SmsManager.getDefault();
                    for(Contacto contacto:contactos)
                    {
                        ArrayList<String> parts = smsManager.divideMessage(msj);
                        smsManager.sendMultipartTextMessage(contacto.getNumeroTelefonico(), null, parts, null, null);
                    }
                    Toast.makeText(this,contactos.size()+" elementos fueron invitados mediante SMS",Toast.LENGTH_LONG).show();
                }

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

    private void showSnackBar() {
        openSettings();
    }

    public void openSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }


    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    SmsManager smsManager = SmsManager.getDefault();
                    for(Contacto contacto:contactos)
                    {
                        ArrayList<String> parts = smsManager.divideMessage(msj);
                        smsManager.sendMultipartTextMessage(contacto.getNumeroTelefonico(), null, parts, null, null);
                    }
                    Toast.makeText(this,contactos.size()+" mensajes de texto enviados.",Toast.LENGTH_LONG).show();
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
}
