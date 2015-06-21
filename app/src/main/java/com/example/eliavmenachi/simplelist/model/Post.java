package com.example.eliavmenachi.simplelist.model;

import java.util.Date;

/**
 * Created by DELL on 6/19/2015.
 */
public class Post {
    String postComment;
    String Id;
    String postTitle;
    Date createdAt;

    // for creation
    public Post(String comment, String title) {
        this.postComment = comment;
        this.postTitle = title;
    }

    // for query
    public Post(String id, String comment, String title, Date createdAt) {
        this.postComment = comment;
        this.Id = id;
        this.postTitle = title;
        this.createdAt = createdAt;
    }

    public String getPost() {
        return postComment;
    }
    public String getTitle() {
        return postTitle;
    }
    public Date getDate() { return createdAt; }

    public void setPost(String comment) {
        this.postComment = comment;
    }
    public void setTitle(String title) {
        this.postTitle = title;
    }
}
