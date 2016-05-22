package com.reptile.nomad.reptile.Models;

import android.util.Log;


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
    public String googleid = "";
    public int TYPE;
    private String userSessionToken;

    public User(String firstName, String lastName ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = firstName + " "+ lastName;
    }
    public User(String firstName, String lastName, String facebookid ){
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = firstName + " "+ lastName;
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
            switch (input.getString("type"))
            {
                case "facebook":
                    newUser.facebookid = input.getString("facebookid");
                    newUser.TYPE = Reptile.FACEBOOK_LOGIN;
                    break;
                case "google":
                    newUser.googleid = input.getString("googleid");
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
    public String getUserName() {
        return userName;
    }


}
