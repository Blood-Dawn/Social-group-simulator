package org.campusboard.sgs.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a post in the social group simulator.
 * Contains information such as title, body, likes, dislikes, and a unique identifier.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @JsonCreator
    public Post(
            @JsonProperty("id") UUID id,
            @JsonProperty("title") String title,
            @JsonProperty("body") String body,
            @JsonProperty("category") Category category,
            @JsonProperty("likes") int likes,
            @JsonProperty("dislikes") int dislikes,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("author") String author) {
        this.title = title;
        this.body = body;
        this.id = id == null ? UUID.randomUUID() : id;
        this.category = category == null ? Category.GENERAL : category;
        this.likes = likes;
        this.dislikes = dislikes;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.author = author;
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
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
