package com.example.laura.planit.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.laura.planit.Activities.Eventos.TimerActivity;

/**
 * Created by Laura on 18/09/2016.
 */
public class NotificationService extends Service {

    public NotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Intent i = new Intent(this, TimerActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), i, 0);
        Notification n  = new Notification.Builder(this)
                .setContentTitle("Timer")
                .setContentText("Debes terminar el timer")
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .addAction(android.R.drawable.ic_dialog_alert, "Call", pIntent).build();;

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        notificationManager.notify(0,n);
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
}
