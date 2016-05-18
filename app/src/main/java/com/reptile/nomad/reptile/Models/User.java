package com.reptile.nomad.reptile.Models;

/**
 * Created by nomad on 12/5/16.
 */
public class User {
    public String userName;
    public String firstName;
    public String lastName;
    public String id=null;
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
    }
    public String getId()
    {
        if(id==null)
        {
            return facebookid;
        }
        else
        {
            return id;
        }
    }
    public String getUserName() {
        return userName;
    }


}
