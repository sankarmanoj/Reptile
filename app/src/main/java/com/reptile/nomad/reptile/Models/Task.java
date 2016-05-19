package com.reptile.nomad.reptile.Models;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by nomad on 11/5/16.
 */
public class Task {


    public enum Status { ACTIVE , DONE, MISSED }
//    public User creator;
    public int userId;
    public String username;
    private String taskString;
    public int id;
    private int likes;
    public int comments;
//    private List<Comment> comments;
    private String deadline;
    private String created;
    private Status status;
//    private List<User> visibleTo;


    public Task(){

    }
    public Task(int userId, String username, String taskString, int id, int likes, int comments, String deadline, String created){
        this.userId = userId;
        this.username = username;
        this.taskString = taskString;
        this.id = id;
        this.likes = likes;
        this.comments = comments;
        this.deadline = deadline;
        this.created = created;
        this.status = Status.ACTIVE;
    }

    public JSONObject createdTask()
    {
        JSONObject toSend = new JSONObject();
        try
        {
            toSend.put("created",created);
//            toSend.put("creator",creator.getId());
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

    public void setId(int id) {
        this.id = id;
    }

    public int getLikes() {
        return likes;
    }

    public String getUsername(){return username; }

    public void setLikes(int likes) {
        this.likes = likes;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }






}
