package com.example.laura.planit.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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
