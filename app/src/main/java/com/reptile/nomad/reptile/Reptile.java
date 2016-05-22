package com.reptile.nomad.reptile;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
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
    public static final int FACEBOOK_LOGIN = 594;
    public static final int GOOGLE_LOGIN = 771;
    public static Socket mSocket;
    public static Reptile Instance;
    public static boolean connectedToServer = false;
    public static String DeviceID;
    public final static String TAG = "Reptile Application";
    public static User mUser;
    public static HashMap<String,Task> mOwnTasks;
    public static HashMap<String,User> knownUsers;
    public static GoogleApiClient mGoogleApiClient;
    public static GoogleSignInAccount mGoogleAccount;
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
                if(FacebookSdk.isInitialized()&&loginMethod()==FACEBOOK_LOGIN)
                {
                    facebookLogin();
                }
                else if(loginMethod()==GOOGLE_LOGIN)
                {

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
                    Log.d(TAG,"Broadcast Tasks Updated "+String.valueOf(mOwnTasks.size()));
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(QuickPreferences.tasksUpdated));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        });
        mSocket.on("login", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                mUser.id=(String)args[0];
                Log.d(TAG,"User ID "+mUser.id);
            }
        });
    }
    public static void googleLogin(GoogleSignInAccount account)
    {
        JSONObject toSendToServer = new JSONObject();
        try
        {
            toSendToServer.put("firstname",account.getDisplayName().split(" ")[0]);
            toSendToServer.put("lastname",account.getDisplayName().split(" ")[account.getDisplayName().split(" ").length-1]);
            toSendToServer.put("deviceid",DeviceID);
            toSendToServer.put("accesstoken",account.getIdToken());
            toSendToServer.put("type","google");
            toSendToServer.put("googleid",account.getId());


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        mUser = new User(account.getDisplayName().split(" ")[0],account.getDisplayName().split(" ")[account.getDisplayName().split(" ").length-1]);
        mUser.googleid = account.getId();
        Log.d(TAG,"ID Token +"+account.getIdToken());
        Log.d(TAG,"Google Login\n "+toSendToServer.toString());
        mSocket.emit("login",toSendToServer);
    }
    public static void facebookLogin()
    {
        if(Profile.getCurrentProfile()!=null) {
            JSONObject toSendToServer = new JSONObject();
            try {

                toSendToServer.put("deviceid", DeviceID);
                toSendToServer.put("type","facebook");
                toSendToServer.put("facebookid", Profile.getCurrentProfile().getId());
                toSendToServer.put("accesstoken", AccessToken.getCurrentAccessToken().getToken());
                toSendToServer.put("firstname", Profile.getCurrentProfile().getFirstName());
                toSendToServer.put("lastname", Profile.getCurrentProfile().getLastName());
                Log.i("User Registration",toSendToServer.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mUser = new User(Profile.getCurrentProfile().getFirstName(),Profile.getCurrentProfile().getLastName());
            mUser.facebookid = Profile.getCurrentProfile().getId();
            mSocket.emit("login",toSendToServer);

        }
    }

    public static  boolean checkLoggedIn()
    {
        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(Instance.getApplicationContext());

        switch (sharedPreferences.getString(QuickPreferences.loginType,"null"))
        {
            case  "null":
                return false;

            case QuickPreferences.facebookLogin:
                //TODO Implent facebook Login Check
                return true;

            case QuickPreferences.googleLogin:
                //TODO Implement Google Login Check
                return true;

            default:
                return false;



        }


    }
    public static int loginMethod()
    {
        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(Instance.getApplicationContext());

        switch (sharedPreferences.getString(QuickPreferences.loginType,"null"))
        {


            case QuickPreferences.facebookLogin:
                return FACEBOOK_LOGIN;


            case QuickPreferences.googleLogin:
                return GOOGLE_LOGIN;

            default:
                return 0;


        }
    }
}
