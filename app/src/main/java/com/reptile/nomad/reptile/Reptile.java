package com.reptile.nomad.reptile;

import android.app.Application;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by sankarmanoj on 13/05/16.
 */
public class Reptile extends Application {
    public static Socket mSocket;
    public static Reptile Instance;
    public static boolean connectedToServer = false;
    public  String DeviceID;
    public final String TAG = "Reptile Application";
    private RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        Instance=this;
        DeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        URI serverURI = null;
        try {
           serverURI = new URI(getString(R.string.server_uri));
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
        mSocket = IO.socket(serverURI);
        mSocket.connect();
        mSocket.on(Socket.EVENT_CONNECT,new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                connectedToServer = true;

                Log.d(TAG,"Socket Connected");
            }
        });
        mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                connectedToServer = false;
                Log.d(TAG, "Socket Disconnected");
            }
        });

    }
    public void facebookLogin()
    {
        if(Profile.getCurrentProfile()!=null) {
            JSONObject toSendToServer = new JSONObject();
            try {

                toSendToServer.put("deviceid", DeviceID);
                toSendToServer.put("facebookid", Profile.getCurrentProfile().getId());
                toSendToServer.put("accesstoken", AccessToken.getCurrentAccessToken().getToken());
                toSendToServer.put("firstname", Profile.getCurrentProfile().getFirstName());
                toSendToServer.put("lastname", Profile.getCurrentProfile().getLastName());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSocket.emit("login",toSendToServer);
            Log.d(TAG,toSendToServer.toString());
        }
    }
    public static synchronized Reptile getInstance() {
        return Instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
