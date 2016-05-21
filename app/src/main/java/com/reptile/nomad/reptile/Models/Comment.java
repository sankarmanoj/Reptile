package com.reptile.nomad.reptile.Models;

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

    private User author;
    private String comment;
    private double comment_likes;
    private boolean hide = false; // If the user deletes the comment of others, change this to true

    public Comment(String comment, User author,double comment_likes){
        this.comment = comment;
        this.comment_likes = comment_likes;
        this.author = author;
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

    public String getAuthorname(){
        return author.getUserName();
    }

}
