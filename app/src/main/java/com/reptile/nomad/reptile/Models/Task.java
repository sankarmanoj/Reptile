package com.reptile.nomad.reptile.Models;

import android.support.annotation.Nullable;

import com.reptile.nomad.reptile.Reptile;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by nomad on 11/5/16.
 */
public class Task {
    public enum Status { ACTIVE , DONE, MISSED }
    public User creator;
    private String taskString;
    public String id;
    private int likes;
    private List<Comment> comments;
    private Calendar deadline;
    private Calendar created;
    private Status status;
    private List<User> visibleTo;


    public Task(User creator, String taskString,    Calendar created, Calendar deadline){
        this.created = created;
        this.deadline = deadline;
        this.taskString = taskString;
        this.creator = creator;
    }
    public static void addTask(JSONObject input)
    {
        try
        {
            String id = input.getString("_id");
            if(Reptile.mOwnTasks.get(id)!=null) return;
            String creatordid = input.getString("creator");
            User creator = Reptile.knownUsers.get(creatordid);
            if(creator==null)
            {
                //TODO : Implement Look up User and Add
            }
            else
            {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-d'T'k:m:s.S'Z'");
                Calendar created = Calendar.getInstance();
                created.setTime(simpleDateFormat.parse(input.getString("created")));
                Calendar deadline = Calendar.getInstance();
                deadline.setTime(simpleDateFormat.parse(input.getString("deadline")));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }
    public JSONObject getJSON()
    {
        JSONObject toSend = new JSONObject();
        try
        {
            toSend.put("created",created.getTime());
            toSend.put("creator",creator.facebookid);
            toSend.put("taskstring",taskString);
            toSend.put("deadline",deadline.getTime());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return  toSend;
    }


    public String getTaskString() {
        return taskString;
    }

    public void setTaskString(String taskString) {
        this.taskString = taskString;
    }


    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }






}
