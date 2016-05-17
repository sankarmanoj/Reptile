package com.reptile.nomad.reptile.Models;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by nomad on 11/5/16.
 */
public class Task {
    public User creator;
    private String taskString;
    private long id;
    private int likes;
    private List<Comment> comments;
    private String dueTime;
    private String created;
    private String status; // "ACTIVE" "DONE" "MISSED"
    private List<User> visibleTo;


    public Task(User creator, String taskString, String status, long id, int likes, @Nullable List<Comment> comments,@Nullable String dueTime,@Nullable String created){
        this.comments = comments;
        this.id = id;
        this.created = created;
        this.dueTime = dueTime;
        this.taskString = taskString;
        this.likes = likes;
        this.status = status;
        this.creator = creator;
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

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
