package com.reptile.nomad.reptile.Services;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.reptile.nomad.reptile.R;

/**
 * Created by sankarmanoj on 30/05/16.
 */
public class MyMessagingService extends FirebaseMessagingService {
    private static final String TAG ="MyMessagingService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG,remoteMessage.getData().toString());
        if(remoteMessage.getData().get("type").equals("notification"))
        {

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
            mBuilder.setContentTitle(remoteMessage.getData().get("title")).setContentText(remoteMessage.getData().get("body"));

            NotificationManager mNotifManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            mBuilder.setSmallIcon(R.drawable.gecko_vector);
            mNotifManager.notify(123,mBuilder.build());
        }
    }
}


