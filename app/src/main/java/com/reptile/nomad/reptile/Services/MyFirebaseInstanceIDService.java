package com.reptile.nomad.reptile.Services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.reptile.nomad.reptile.Reptile;

/**
 * Created by sankarmanoj on 30/05/16.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"Refreshed Token ="+refreshedToken);
        Reptile.mSocket.emit("fcm-token",refreshedToken);
    }
}
