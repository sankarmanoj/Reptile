package com.reptile.nomad.reptile;

import android.app.Application;
import android.provider.Settings;
import android.util.Log;

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
}
