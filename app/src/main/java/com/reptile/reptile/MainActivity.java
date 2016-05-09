package com.reptile.reptile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
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

                        Log.d("Initiated", response);
                    }

                    @Override
                    public void onInitiationFailed(Exception paramException) {
                        Log.e("Initiation Error", paramException.getMessage());
                    }

                    @Override
                    public void onVerified(String response) {
                        Log.d("Verified", response);
                        JSONObject toSend = new JSONObject();
                        try{
                            toSend.put("phonenumber",phoneNumber.getText().toString());
                            toSend.put("countrycode",countryCode.getText().toString());
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        Reptile.mSocket.emit("otpVerified",toSend);
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
