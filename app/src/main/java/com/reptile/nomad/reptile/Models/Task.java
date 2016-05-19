package com.reptile.nomad.reptile.Models;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

/**
 * Created by nomad on 11/5/16.
 */
public class Task {
    public enum Status { ACTIVE , DONE, MISSED }
    public User creator;
    private String taskString;
    public long id;
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
        this.status = status;
        this.creator = creator;
    }

    public JSONObject createdTask()
    {
        JSONObject toSend = new JSONObject();
        try
        {
            toSend.put("created",created);
            toSend.put("creator",creator.getId());
            toSend.put("taskstring",taskString);
            toSend.put("deadline",deadline);
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }






}
