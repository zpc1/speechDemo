package com.skylead.speechdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private WeakUp myweakup = null;
    private static final String TAG = "ActivityWakeUp";
    
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "service start");
        if (myweakup == null) {
            myweakup = new WeakUp();
            Log.d(TAG, "service start");
            myweakup.init(getApplicationContext());
            myweakup.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "service stop");

        if (myweakup != null) {
            myweakup.stop();
            myweakup = null;
        }
        super.onDestroy();
    }
}
