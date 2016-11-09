package com.example.laura.planit.Activities.Eventos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.laura.planit.Modelos.PlanIt;
import com.example.laura.planit.R;
import com.example.laura.planit.Services.NotificationService;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Laura on 18/09/2016.
 */
public class TimerActivity extends Activity implements View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=1;
    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = false;
    private Button startB;
    public TextView text;
    private long startTime;
    private final long interval = 1;
    private long faltante = 1000;
    private EmergencyTimer emergencyTimer;

    private final Context context= this;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int tiempo = (int) getIntent().getExtras().get("Tiempo");
        setContentView(R.layout.activity_timer);
        startTime=tiempo*1000;
        faltante=startTime;
        startB = (Button) this.findViewById(R.id.button);
        startB.setOnClickListener(this);
        if(faltante==0)
            startB.setText("TERMINAR");
        text = (TextView) this.findViewById(R.id.timer);
        countDownTimer = new MyCountDownTimer(startTime, interval);
        text.setText(text.getText() + String.valueOf(startTime / 1000));
    }

    @Override
    public void onClick(View v) {
        if(faltante==0)
        {
            text.setText("Terminado!");
            if(emergencyTimer!=null)
                emergencyTimer.cancel();
            startB.setEnabled(false);
        }
        else if (!timerHasStarted) {
            countDownTimer.start();
            timerHasStarted = true;
            startB.setText("STOP");
        } else {
            countDownTimer.cancel();
            timerHasStarted = false;
            startB.setText("RESTART");
        }

    }

    public void enviarMensaje()
    {
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
            SmsManager smsManager = SmsManager.getDefault();
            if(PlanIt.darInstancia().darContactos()!=null)
            smsManager.sendTextMessage(PlanIt.darInstancia().darContactos().get(0).getNumeroTelefonico(), null, "Número de confirmación:", null, null);

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
                    SmsManager smsManager = SmsManager.getDefault();
                    if(PlanIt.darInstancia().darContactos()!=null)
                    smsManager.sendTextMessage(PlanIt.darInstancia().darContactos().get(0).getNumeroTelefonico(), null, "El usuario no responde", null, null);
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

    public class EmergencyTimer extends CountDownTimer {

        public EmergencyTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            Log.v("Intenta enviar", "mensaje urgencia");
            enviarMensaje();
        }
    }
    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            text.setText("Time's up!");
            Intent intent= new Intent(context, NotificationService.class);
            startService(intent);
            emergencyTimer= new EmergencyTimer(20*1000,1000);
            startB.setText("TERMINAR");
        }


        @Override
        public void onTick(long millisUntilFinished) {
            text.setText("" + millisUntilFinished / 1000);
            faltante=millisUntilFinished/1000;
        }
    }
}
