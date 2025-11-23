package org.campusboard.sgs.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Simple comment entity tied to a post.
 */
public final class Comment {
  private final UUID id;
  private final UUID postId;
  private final String author;
  private final String body;
  private final LocalDateTime createdAt;

  public Comment(UUID id, UUID postId, String author, String body, LocalDateTime createdAt) {
    this.id = id == null ? UUID.randomUUID() : id;
    this.postId = Objects.requireNonNull(postId, "postId must not be null");
    this.author = normalizeAuthor(author);
    this.body = requireNonBlank(body, "body");
    this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
  }

  public UUID id() {
    return id;
  }

  public UUID postId() {
    return postId;
  }

  public String author() {
    return author;
  }

  public String body() {
    return body;
  }

  public LocalDateTime createdAt() {
    return createdAt;
  }

  private String normalizeAuthor(String value) {
    if (value == null || value.isBlank()) {
      return Post.GUEST_AUTHOR_FALLBACK;
    }
    return value.trim();
  }

  private String requireNonBlank(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value.trim();
  }
}
