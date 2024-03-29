package com.reptile.nomad.reptile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    GoogleSignInOptions GSO;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInAccount mGoogleAccount = null;

    @Bind(R.id.login_button)
    LoginButton loginButton;

    CallbackManager callbackManager;

    @Bind(R.id.fb_sign_in_button)
    ImageView fb;

    ImageView signInButton;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Activity Result", String.valueOf(requestCode));

        if(requestCode==9001)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            mGoogleAccount = result.getSignInAccount();
            Reptile.mGoogleAccount = mGoogleAccount;
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences.edit()
                    .putString(QuickPreferences.accountid,mGoogleAccount.getId())
                    .putString(QuickPreferences.accesstoken,mGoogleAccount.getIdToken())
                    .putString("pictureURI",mGoogleAccount.getPhotoUrl().toString())
                    .putString("fullname",mGoogleAccount.getDisplayName())
                    .apply();
            Reptile.googleSignUp(mGoogleAccount);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            if (AccessToken.getCurrentAccessToken() != null) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FacebookSdk.isInitialized())
        {
            if(AccessToken.getCurrentAccessToken()!=null)
            {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GSO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile().requestIdToken(getString(R.string.google_server_client_id)).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
        .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(ConnectionResult connectionResult) {

            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API,GSO).build();


        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "Login Success!");
                SharedPreferences.Editor editor =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString(QuickPreferences.accesstoken,loginResult.getAccessToken().getToken());
                editor.putString(QuickPreferences.accountid, loginResult.getAccessToken().getUserId());
                editor.putString(QuickPreferences.tokenExpiry,loginResult.getAccessToken().getExpires().toString());
                editor.putString(QuickPreferences.loginType,QuickPreferences.facebookLogin);
                editor.putString("pictureURI",Profile.getCurrentProfile().getProfilePictureUri(400,400).toString());
                editor.apply();
                Reptile.facebookSignUp();
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
        signInButton = (ImageView) findViewById(R.id.google_sign_in_button);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                SharedPreferences.Editor editor =  PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putString(QuickPreferences.loginType,QuickPreferences.googleLogin);

                editor.apply();
                startActivityForResult(signInIntent,9001);
            }
        });

    }
    public void onClick(View v) {

            loginButton.performClick();

    }




}
