package com.example.eliavmenachi.simplelist.model;

/**
 * Created by DELL on 6/19/2015.
 */
public class Post {
    String postComment;
    String Id;
    String postTitle;

    public Post(String comment, String title) {
        this.postComment = comment;
        this.postTitle = title;
    }

    public Post(String id, String comment, String title) {
        this.postComment = comment;
        this.Id = id;
        this.postTitle = title;
    }

    public String getPost() {
        return postComment;
    }
    public String getTitle() {
        return postTitle;
    }

    public void setPost(String comment) {
        this.postComment = comment;
    }
    public void setTitle(String title) {
        this.postTitle = title;
    }
}
