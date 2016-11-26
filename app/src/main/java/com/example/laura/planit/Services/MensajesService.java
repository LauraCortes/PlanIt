package com.example.laura.planit.Services;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

import com.example.laura.planit.Activities.Main.Constants;
import com.example.laura.planit.Modelos.Contacto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Usuario on 18/09/2016.
 */
public class MensajesService extends IntentService
{
    private List<Contacto> contactos;
    private String msj;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=1;

    public MensajesService()
    {
        super("");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MensajesService(String name) {
        super(name);
    }




    @Override
    protected void onHandleIntent(Intent intent)
    {
        contactos = (List<Contacto>)intent.getExtras().get(Constants.EXTRA_CONTACTOS_SMS);
        msj = intent.getExtras().getString(Constants.EXTRA_SMS);
        if(contactos!=null && msj!=null)
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
                SmsManager smsManager = SmsManager.getDefault();
                for(Contacto contacto:contactos)
                {
                    ArrayList<String> parts = smsManager.divideMessage(msj);
                    smsManager.sendMultipartTextMessage(contacto.getNumeroTelefonico(), null, parts, null, null);
                }
                Toast.makeText(this,"Se enviaron "+contactos.size()+" SMS",Toast.LENGTH_LONG).show();
            }
        }
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
