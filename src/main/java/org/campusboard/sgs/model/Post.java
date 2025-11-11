package org.campusboard.sgs.model;

import java.time.Instant;
import java.util.*;

public final class Post {
  private final UUID id;
  private String title;
  private String body;
  private Category category;
  private final String author;         // username
  private final Instant createdAt;
  private Instant updatedAt;
  private final Set<String> likedBy = new HashSet<>();

  public Post(UUID id, String title, String body, Category cat, String author) {
    this.id = id == null ? UUID.randomUUID() : id;
    this.title = title;
    this.body = body;
    this.category = cat;
    this.author = author;
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
  }
  public UUID id() { return id; }
  public String title() { return title; }
  public String body() { return body; }
  public Category category() { return category; }
  public String author() { return author; }
  public int likeCount() { return likedBy.size(); }
  public boolean isLikedBy(String userId) { return likedBy.contains(userId); }

  public void edit(String title, String body, Category cat) {
    this.title = title; this.body = body; this.category = cat; this.updatedAt = Instant.now();
  }
  /** Toggle like; returns true if now liked. */
  public boolean toggleLike(String userId) {
    if (likedBy.remove(userId)) return false;
    likedBy.add(userId); return true;
  }
}
