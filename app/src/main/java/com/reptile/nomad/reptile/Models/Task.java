package com.reptile.nomad.reptile.Models;

import java.util.List;

/**
 * Created by nomad on 11/5/16.
 */
public class Task {
    private User author;
    private String taskString;
    private long id;
    private int likes;
    private List<Comment_normal> comment_normals;
    private List<Comment_therefore> comment_therefores;
    private String dueTime;
    private String created;
    private String status; // "ACTIVE" "DONE" "MISSED"
    private List<User> visibleTo;


    public Task(String taskString, String status, long id, int likes, List comment_normals, List comment_therefores, String dueTime, String created){
        this.comment_normals = comment_normals;
        this.comment_therefores = comment_therefores;
        this.id = id;
        this.created = created;
        this.dueTime = dueTime;
        this.taskString = taskString;
        this.likes = likes;
        this.status = status;

    }
    public Task(String taskString, String status, long id, int likes, List comment_normals, List comment_therefores, String dueTime, String created,List visibleTo){
        this.comment_normals = comment_normals;
        this.comment_therefores = comment_therefores;
        this.id = id;
        this.created = created;
        this.dueTime = dueTime;
        this.taskString = taskString;
        this.likes = likes;
        this.status = status;
        this.visibleTo = visibleTo;
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

    public List<Comment_normal> getComment_normals() {
        return comment_normals;
    }

    public void setComment_normals(List<Comment_normal> comment_normals) {
        this.comment_normals = comment_normals;
    }

    public List<Comment_therefore> getComment_therefores() {
        return comment_therefores;
    }

    public void setComment_therefores(List<Comment_therefore> comment_therefores) {
        this.comment_therefores = comment_therefores;
    }
}
