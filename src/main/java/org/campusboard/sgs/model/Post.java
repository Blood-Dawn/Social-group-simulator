package org.campusboard.sgs.model;

import java.time.LocalDateTime;
import java.util.Objects;
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
    // Store a concrete User so posts always have traceable ownership information.
    private User author;

    public Post(String title, String body, User author) {
        this(title, body, Category.GENERAL, author);
    }

    public Post(String title, String body, Category category, User author) {
        this.title = title;
        this.body = body;
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.category = category == null ? Category.GENERAL : category;
        // Ensure every post is constructed with a real author reference.
        this.author = Objects.requireNonNull(author, "Author cannot be null");
    }

    // Getters
    public String getTitle() { return title; }
    public String getBody() { return body; }
    public UUID getId() { return id; }
    public Category getCategory() { return category; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public User getAuthor() { return author; }
    
    public int getDislikes() { return dislikes; }
    public int getLikes() { return likes; }
    
    // Setters
    public void setDislikes(int dislikes) { this.dislikes = dislikes; }
    public void setLikes(int likes) { this.likes = likes; }
    public void setCategory(Category category) { this.category = category; }
    public void setAuthor(User author) {
        // Setter also defends against null assignments to keep ownership intact.
        this.author = Objects.requireNonNull(author, "Author cannot be null");
    }
}
