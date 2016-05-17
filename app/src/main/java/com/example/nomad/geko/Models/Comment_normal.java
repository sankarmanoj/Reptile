package com.example.nomad.geko.Models;

/**
 * Created by nomad on 11/5/16.
 */
public class Comment_normal {
    private String comment;
    private double comment_likes;
    private boolean hide = false; // If the user deletes the comment of others, change this to true

    public Comment_normal(String comment, double comment_likes){
        this.comment = comment;
        this.comment_likes = comment_likes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getComment_likes() {
        return comment_likes;
    }

    public void setComment_likes(double comment_likes) {
        this.comment_likes = comment_likes;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }
}
