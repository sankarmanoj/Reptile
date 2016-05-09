package com.reptile.reptile;

import android.app.Application;
import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by sankarmanoj on 09/05/16.
 */
public class Reptile extends Application {
    public  static Socket mSocket;
    public static boolean socketConnected = false;
    @Override
    public void onCreate() {
        super.onCreate();
        try
        {
            mSocket = IO.socket("https://sankar-manoj.com");
        }
        catch(URISyntaxException e){e.printStackTrace();}
        mSocket.connect();
        mSocket.on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("Socket Error", "ERR");
            }
        });
        mSocket.on(Socket.EVENT_CONNECT,new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socketConnected = true;
                Log.d("Socket Event","Connected");
            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mSocket.close();
        mSocket.disconnect();
    }
}
