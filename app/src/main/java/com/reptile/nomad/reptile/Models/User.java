package com.reptile.nomad.reptile.Models;

import android.util.Log;

import com.android.volley.toolbox.StringRequest;
import com.reptile.nomad.reptile.Reptile;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nomad on 12/5/16.
 */
public class User {
    public String userName;
    public String firstName;
    public String lastName;
    public String id="";
    public String facebookid = "";
    public boolean self;
    private String userSessionToken;

    public User(String firstName, String lastName ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = firstName + " "+ lastName;
    }
    public User(String firstName, String lastName, boolean self, String facebookid ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = firstName + " "+ lastName;
        this.self=self;
        this.facebookid = facebookid;
    }
    public static void addUser(JSONObject input)
    {

        try
        {
            String id = input.getString("_id");
            if(Reptile.knownUsers.get(id)!=null) return;
            User newUser = new User(input.getString("firstname"),input.getString("lastname"));
            newUser.id=input.getString("_id");
            newUser.facebookid = input.getString("facebookid");
            Reptile.knownUsers.put(id,newUser);
//            Log.d("User",newUser.userName);

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
