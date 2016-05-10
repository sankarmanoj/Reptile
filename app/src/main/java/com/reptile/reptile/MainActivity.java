package com.reptile.reptile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.msg91.sendotp.library.Config;
import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;
import com.msg91.sendotp.library.internal.Iso2Phone;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends Activity {

    Socket mSocket;
    EditText phoneNumber;
    EditText countryCode;
    @InjectView(R.id.nameRelativeLayout)
    RelativeLayout nameRelativeLayout;
    @InjectView(R.id.nameEditText)
    EditText nameEditText;
    @InjectView(R.id.nameButton)
    Button nameButton;
    @InjectView(R.id.numberRelativeLayout)
    RelativeLayout numberRelativeLayout;
    @InjectView(R.id.otpRelativeLayout)
    RelativeLayout otpRelativeLayout;
    @InjectView(R.id.otpEditText)
    EditText otpEditText;
    @InjectView(R.id.checkOTPButton)
    Button checkOTPButton;
    @InjectView(R.id.enterOTPTextView)
    TextView otpTextView;
    Button sendOTP;
    Verification mVertification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phoneNumber = (EditText)findViewById(R.id.editText);
        countryCode = (EditText)findViewById(R.id.editText2);
        ButterKnife.inject(this);

        sendOTP =(Button)findViewById(R.id.button);
        tryAndPrefillPhoneNumber(phoneNumber);
        checkOTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String OTP = otpEditText.getText().toString();
                mVertification.verify(OTP);
                Reptile.mSocket.emit("client-OTP",OTP);
            }
        });
        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Config config = SendOtpVerification.config().context(getApplicationContext()).build();

                mVertification = SendOtpVerification.createSmsVerification(config, phoneNumber.getText().toString(), new VerificationListener() {
                    @Override
                    public void onInitiated(String response) {

                        otpEditText.setVisibility(View.VISIBLE);
                        checkOTPButton.setVisibility(View.VISIBLE);
                        otpTextView.setVisibility(View.VISIBLE);

                        sendOTP.setEnabled(false);
                        phoneNumber.setEnabled(false);
                        countryCode.setEnabled(false);
                        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);

                        JSONObject toSend = new JSONObject();
                        try{
                            toSend.put("phonenumber",phoneNumber.getText().toString());
                            toSend.put("countrycode",countryCode.getText().toString());
                            toSend.put("androidid",android_id);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        Reptile.mSocket.emit("otpRequest",toSend);
                        Log.d("Initiated", response);
                        Reptile.mSocket.on("otpRequest",new Emitter.Listener() {
                            @Override
                            public void call(Object... args) {
                                final JSONObject messageJSON = (JSONObject)args[0];
                                try{
                                if(messageJSON.getBoolean("alreadyRegistered"))
                                {

                                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                }}
                                catch (JSONException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onInitiationFailed(Exception paramException) {
                        Log.e("Initiation Error", paramException.getMessage());
                    }

                    @Override
                    public void onVerified(String response) {
                        Log.d("Verified", response);
                        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);

                        JSONObject toSend = new JSONObject();
                        try{
                            toSend.put("phonenumber",phoneNumber.getText().toString());
                            toSend.put("countrycode",countryCode.getText().toString());
                            toSend.put("androidid",android_id);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                        Reptile.mSocket.emit("otpVerified",toSend);
                        Reptile.mSocket.off("otpRequest");
                        checkOTPButton.setEnabled(false);
                        Toast.makeText(getApplicationContext(),"OTP Verified Successfully",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onVerificationFailed(Exception paramException) {
                        Log.d("Verification Failed", paramException.getMessage());
                        Toast.makeText(getApplicationContext(),"OTP Verified Failed",Toast.LENGTH_LONG).show();
                    }
                }, countryCode.getText().toString());
                mVertification.initiate();
            }
        });
        nameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameButton.setEnabled(false);
                nameEditText.setEnabled(false);
                numberRelativeLayout.setVisibility(View.VISIBLE);
                final int initialPaddingTop = numberRelativeLayout.getPaddingTop();

                Animation a = new Animation() {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {

                        int newTopPadding =(int)((initialPaddingTop)*(1-interpolatedTime));
                        numberRelativeLayout.setPadding(0,newTopPadding,0,0);
                        if(newTopPadding<70)
                        {
                            nameRelativeLayout.setVisibility(View.INVISIBLE);
                        }
                    }
                };
                a.setDuration(600);
               numberRelativeLayout.startAnimation(a);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void tryAndPrefillPhoneNumber(EditText mPhoneNumber) {
        if (checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber.setText(manager.getLine1Number());
        }
    }
}
