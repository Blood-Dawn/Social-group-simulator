package org.campusboard.sgs.model;

import java.time.LocalDateTime;
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
    private Category category;
    private LocalDateTime createdAt;
    private String author; 
    // TODO: Change to User object when User class is implemented

    public Post(String title, String body) {
        this.title = title;
        this.body = body;
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.category = Category.GENERAL; // default category
    }
    
    public Post(String title, String body, Category category) {
        this.title = title;
        this.body = body;
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.category = category;
    }

    // Getters
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public UUID getId() { return id; }
    public Category getCategory() { return category; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getAuthor() { return author; }
    
    public int getDislikes() { return dislikes; }
    public int getLikes() { return likes; }
    
    // Setters
    public void setDislikes(int dislikes) { this.dislikes = dislikes; }
    public void setLikes(int likes) { this.likes = likes; }
    public void setCategory(Category category) { this.category = category; }
    public void setAuthor(String author) { this.author = author; }
}