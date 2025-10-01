package org.campusboard.sgs.model;

import java.util.UUID;

/**
 * Represents a post in the social group simulator.
 * Contains information such as title, body, likes, dislikes, and a unique identifier.
 */
public class Post {
    private String title;
    private String body;
    private int dislikes;
    private int likes;
    private UUID id;

    public Post(String title, String body) {
        this.title = title;
        this.body = body;
        this.id = UUID.randomUUID();
    }

    public String getTitle() { return title; }
    public String getBody() { return body; }
    
    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public UUID getId() {
        return id;
    }
}