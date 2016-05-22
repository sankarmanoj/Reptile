package com.reptile.nomad.reptile.Models;

import com.reptile.nomad.reptile.Reptile;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by nomad on 11/5/16.
 */
public class Comment {
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User author;
    public Task task;
    public String id;
    public String comment;
    public Calendar created;
    public double comment_likes;
    public boolean hidden = false; // If the user deletes the comment of others, change this to true

    public Comment(String comment, User author, Task task){
        this.comment = comment;
        this.task = task;
        this.author = author;
    }
    public Comment(String comment, User author, Task task, String id){
        this.comment = comment;
        this.task = task;
        this.author = author;
        this.id = id;
    }
    public static Comment generateComment(JSONObject input)
    {
     try
     {
         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-d'T'k:m:s.S'Z'");
      Comment newComment = new Comment(input.getString("commentstring"), Reptile.knownUsers.get(input.getString("creator")),Reptile.mOwnTasks.get(input.getString("task")),input.getString("_id"));
         Calendar created = Calendar.getInstance();
         created.setTime(simpleDateFormat.parse(input.getString("created")));
         newComment.created = created;
         return newComment;

     }
     catch (JSONException e)
     {
         e.printStackTrace();
         throw new RuntimeException("Error Parsing JSON, Unable to Add Comment");
     }
        catch (ParseException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error Parsing JSON, Unable to Add Comment");
        }

    }
    public String getComment() {
        return comment;
    }

    public JSONObject getCreationJSON()
    {
        JSONObject toSend = new JSONObject();
        try
        {
            toSend.put("commentstring",comment);
            toSend.put("task",task.id);
            toSend.put("creator",author.id);
            toSend.put("created", Calendar.getInstance().getTime());
        }catch (JSONException e)
        {
            e.printStackTrace();
        }

        return toSend;
    }

    public String getAuthorname(){
        return author.getUserName();
    }

}
