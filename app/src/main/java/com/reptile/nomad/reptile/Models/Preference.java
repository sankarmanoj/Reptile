package com.reptile.nomad.reptile.Models;

import com.reptile.nomad.reptile.Reptile;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by nomad on 31/5/16.
 */
public class Preference {
    public String tagName;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean status;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }


    public Preference(String tagName, Boolean status) {
        this.tagName = tagName;
        this.status = status;
    }
    public static Preference getPreferenceObject(JSONObject input)
    {
        try
        {
            Preference newPreference = new Preference(input.getString("tag"),input.getBoolean("status"));
            return newPreference;

        }
        catch (JSONException e)
        {
            e.printStackTrace();
            throw new RuntimeException("error");
        }

    }
}
