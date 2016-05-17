package com.reptile.nomad.reptile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    @Bind(R.id.login_button)
    LoginButton loginButton;
    CallbackManager callbackManager;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Activity Result", String.valueOf(requestCode));
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "Login Success!");
                SharedPreferences.Editor editor =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString(QuickPreferences.facebookToken,loginResult.getAccessToken().getToken());
                editor.putString(QuickPreferences.facebookProfile, loginResult.getAccessToken().getUserId());
                editor.putString(QuickPreferences.tokenExpiry,loginResult.getAccessToken().getExpires().toString());
                editor.apply();
                editor.commit();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }

            @Override
            public void onCancel() {
                Log.e(TAG,"Login Cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                error.printStackTrace();
                Log.e(TAG,"Login Error");
            }
        });


    }





}
