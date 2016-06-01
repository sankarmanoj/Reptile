package com.reptile.nomad.reptile.Models;

import android.util.Log;


import com.reptile.nomad.reptile.Reptile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by nomad on 12/5/16.
 */
public class User {
    public String userName;
    public String firstName;
    public String lastName;
    public String id="";
    public String accountid = "";
    public int TYPE;
    private String userSessionToken;

    public User(String firstName, String lastName ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = firstName + " "+ lastName;
    }

    public static void addToKnownUser(JSONObject input)
    {

        try
        {
            String id = input.getString("_id");
            if(Reptile.knownUsers.get(id)!=null) return;
            User newUser = new User(input.getString("firstname"),input.getString("lastname"));
            newUser.id=input.getString("_id");
            newUser.accountid = input.getString("accountid");
            switch (input.getString("type"))
            {
                case "facebook":

                    newUser.TYPE = Reptile.FACEBOOK_LOGIN;
                    break;
                case "google":

                    newUser.TYPE = Reptile.GOOGLE_LOGIN;
                    break;
            }

            Reptile.knownUsers.put(id,newUser);


        }
        catch (JSONException e)
        {
            Log.e("Add User",input.toString());
            e.printStackTrace();
        }

    }
    public static void addUserToHashMap (JSONObject input, HashMap<String,User> userHashMap)
    {

        try
        {
            String id = input.getString("_id");
            User newUser = new User(input.getString("firstname"),input.getString("lastname"));
            newUser.id=input.getString("_id");
            newUser.accountid = input.getString("accountid");
            switch (input.getString("type"))
            {
                case "facebook":

                    newUser.TYPE = Reptile.FACEBOOK_LOGIN;
                    break;
                case "google":

                    newUser.TYPE = Reptile.GOOGLE_LOGIN;
                    break;
            }
            Log.d("Added user ",newUser.userName);
            userHashMap.put(id,newUser);


        }
        catch (JSONException e)
        {
            Log.e("Add User",input.toString());
            e.printStackTrace();
        }

    }
    public String getUserName() {
        return userName;
    }


}
