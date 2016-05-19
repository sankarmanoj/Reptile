package com.reptile.nomad.reptile;

import android.app.Application;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.reptile.nomad.reptile.Models.Task;
import com.reptile.nomad.reptile.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public static User mUser;
    public static HashMap<String,Task> mOwnTasks;
    public static HashMap<String,User> knownUsers;
    @Override
    public void onCreate() {
        super.onCreate();
        Instance=this;
        mOwnTasks = new HashMap<>();
        knownUsers = new HashMap<>();
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
                if(FacebookSdk.isInitialized())
                {
                    facebookLogin();
                }
                Log.d(TAG,"Socket Connected");
            }
        });
        mSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                connectedToServer = false;
                mSocket.connect();
                Log.d(TAG, "Socket Disconnected");
            }
        });
        mSocket.on("addusers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONArray inputArray = new JSONArray((String) args[0]);
                    for(int i = 0; i<inputArray.length();i++)
                    {
                        JSONObject input = inputArray.getJSONObject(i);
                        User.addUser(input);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
        mSocket.on("addtasks", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONArray inputArray = new JSONArray((String) args[0]);
                    for(int i = 0; i<inputArray.length();i++)
                    {
                        JSONObject input = inputArray.getJSONObject(i);
                        Task.addTask(input);
                    }
                    Log.d(TAG,"Broadcast Tasks Updated");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(QuickPreferences.tasksUpdated));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
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
                Log.i("User Registration",toSendToServer.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mUser = new User(Profile.getCurrentProfile().getFirstName(),Profile.getCurrentProfile().getLastName(),true,Profile.getCurrentProfile().getId());
            mSocket.emit("login",toSendToServer);

        }
    }

}
