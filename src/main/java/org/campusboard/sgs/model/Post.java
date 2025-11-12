package org.campusboard.sgs.model;

import java.time.Instant;
import java.util.*;

public final class Post {
  private final UUID id;
  private String title;
  private String body;
  private Category category;
  private final String author;
  private final Instant createdAt;
  private Instant updatedAt;
  private final Set<String> likedBy = new HashSet<>();
  private final Set<String> dislikedBy = new HashSet<>();

  public Post(UUID id, String title, String body, Category category, String author) {
    this.id = id == null ? UUID.randomUUID() : id;
    this.title = Objects.requireNonNull(title, "title");
    this.body = Objects.requireNonNull(body, "body");
    this.category = Objects.requireNonNull(category, "category");
    this.author = Objects.requireNonNull(author, "author");
    this.createdAt = Instant.now();
    this.updatedAt = createdAt;
  }

  public UUID id() { return id; }
  public String title() { return title; }
  public String body() { return body; }
  public Category category() { return category; }
  public String author() { return author; }
  public Instant createdAt() { return createdAt; }
  public Instant updatedAt() { return updatedAt; }
  public int likeCount() { return likedBy.size(); }
  public int dislikeCount() { return dislikedBy.size(); }

  public void setTitle(String title) {
    this.title = Objects.requireNonNull(title, "title");
    this.updatedAt = Instant.now();
  }

  public void setBody(String body) {
    this.body = Objects.requireNonNull(body, "body");
    this.updatedAt = Instant.now();
  }

  public void setCategory(Category category) {
    this.category = Objects.requireNonNull(category, "category");
    this.updatedAt = Instant.now();
  }

  public boolean toggleLike(String userId) {
    Objects.requireNonNull(userId, "userId");
    dislikedBy.remove(userId); // Remove dislike if present (mutually exclusive)
    if (likedBy.remove(userId)) {
      updatedAt = Instant.now();
      return false;
    }
    likedBy.add(userId);
    updatedAt = Instant.now();
    return true;
  }

  public boolean isLikedBy(String userId) {
    return likedBy.contains(userId);
  }

  public boolean toggleDislike(String userId) {
    Objects.requireNonNull(userId, "userId");
    likedBy.remove(userId); // Remove like if present (mutually exclusive)
    if (dislikedBy.remove(userId)) {
      updatedAt = Instant.now();
      return false;
    }
    dislikedBy.add(userId);
    updatedAt = Instant.now();
    return true;
  }

  public boolean isDislikedBy(String userId) {
    return dislikedBy.contains(userId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Post)) return false;
    return id.equals(((Post) o).id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "Post{id=" + id + ", title='" + title + "', author='" + author + "'}";
  }
}
