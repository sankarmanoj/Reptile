package com.reptile.nomad.reptile.Models;

import android.support.annotation.Nullable;

import com.reptile.nomad.reptile.Reptile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
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
    public LinkedHashMap<String, User> likers;
    public List<Comment> comments;
    private Calendar deadline;
    private Calendar created;
    private Status status;
    public List<Group> visibleTo;
    public boolean publictask;

    public Task(User creator, String taskString,    Calendar created, Calendar deadline){
        this.created = created;
        this.deadline = deadline;
        this.taskString = taskString;
        this.creator = creator;
        likers = new LinkedHashMap<>();
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
                Task newTask = new Task(creator,input.getString("taskstring"),created,deadline);
                JSONArray membersJSON = input.getJSONArray("likers");
                for (int i = 0; i<membersJSON.length();i++)
                {
                    newTask.likers.put(membersJSON.getString(i),Reptile.knownUsers.get(membersJSON.getString(i)));
                }
                newTask.id=id;
                Reptile.mOwnTasks.put(id,newTask);

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
            toSend.put("creator",creator.id);
            toSend.put("taskstring",taskString);
            toSend.put("deadline",deadline.getTime());
//            toSend.put("likes",likes);
//            JSONArray JsonLikers = new JSONArray();
//            for(User liker : this.likers.values())
//            {
//                JsonLikers.put(liker.id);
//            }
//            toSend.put("members",JsonLikers);
            toSend.put("publictask",publictask);
            if(publictask==false)
            {
                JSONArray visiblegroups = new JSONArray();
               for(Group group : visibleTo)
               {
                   visiblegroups.put(group.id);
               }
                toSend.put("visibilegroups",visiblegroups);
            }

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

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Calendar getDeadline() {
        return deadline;
    }

    public void setDeadline(Calendar deadline) {
        this.deadline = deadline;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public boolean likedByCurrentUser(){
        return likers.containsValue(Reptile.mUser);
    }
    public String getLikes(){
        int likes;
        if (!likers.isEmpty()) {
            likes = likers.size();
        } else {
            likes = 0;
        }
        String likesString = Integer.toString(likes);
        likesString = likesString + " ";
        return likesString;
    }



}
