package org.campusboard.sgs.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a post in the social group simulator.
 * Maintains per-user likes to support toggle-like behavior.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
  public static final String GUEST_AUTHOR_FALLBACK = "guest";

  private final UUID id;
  private String title;
  private String body;
  private Category category;
  private final String author;
  private final LocalDateTime createdAt;
  private final Set<String> likedBy = new HashSet<>();

  public Post(UUID id, String title, String body, Category category, String author) {
    this(id, title, body, category, author, null, null);
  }

  public Post(String title, String body, Category category, String author) {
    this(null, title, body, category, author, null, null);
  }

  public Post(String title, String body, Category category, User author) {
    this(null, title, body, category, author == null ? null : author.username(), null, null);
  }

  public Post(String title, String body, User author) {
    this(null, title, body, Category.ANNOUNCEMENTS, author == null ? null : author.username(), null, null);
  }

  @JsonCreator
  public Post(
      @JsonProperty("id") UUID id,
      @JsonProperty("title") String title,
      @JsonProperty("body") String body,
      @JsonProperty("category") Category category,
      @JsonProperty("author") String author,
      @JsonProperty("createdAt") LocalDateTime createdAt,
      @JsonProperty("likedBy") Set<String> likedBy) {
    this.id = id == null ? UUID.randomUUID() : id;
    this.title = requireNonBlank(title, "title");
    this.body = requireNonBlank(body, "body");
    this.category = Objects.requireNonNullElse(category, Category.ANNOUNCEMENTS);
    this.author = normalizeAuthor(author);
    this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    if (likedBy != null && !likedBy.isEmpty()) {
      this.likedBy.addAll(likedBy);
    }
  }

  public UUID id() {
    return id;
  }

  public String title() {
    return title;
  }

  public String body() {
    return body;
  }

  public Category category() {
    return category;
  }

  public String author() {
    return author;
  }

  public LocalDateTime createdAt() {
    return createdAt;
  }

  public int likeCount() {
    return likedBy.size();
  }

  public boolean isLikedBy(String userId) {
    String normalized = normalizeUserId(userId);
    return !normalized.isEmpty() && likedBy.contains(normalized);
  }

  /**
   * Toggles like for the given user.
   *
   * @return true if the post is liked after the toggle; false if unliked
   */
  public boolean toggleLike(String userId) {
    String normalized = normalizeUserId(userId);
    if (normalized.isEmpty()) {
      return false;
    }
    if (likedBy.contains(normalized)) {
      likedBy.remove(normalized);
      return false;
    }
    likedBy.add(normalized);
    return true;
  }

  public void setTitle(String title) {
    this.title = requireNonBlank(title, "title");
  }

  public void setBody(String body) {
    this.body = requireNonBlank(body, "body");
  }

  public void setCategory(Category category) {
    this.category = Objects.requireNonNull(category, "category must not be null");
  }

  // Legacy-style getters for UI code that still calls bean-style accessors.
  public String getTitle() {
    return title();
  }

  public String getBody() {
    return body();
  }

  public UUID getId() {
    return id();
  }

  public Category getCategory() {
    return category();
  }

  public LocalDateTime getCreatedAt() {
    return createdAt();
  }

  public String getAuthor() {
    return author();
  }

  public int getLikes() {
    return likeCount();
  }

  private String normalizeAuthor(String author) {
    if (author == null || author.isBlank()) {
      return GUEST_AUTHOR_FALLBACK;
    }
    return author.trim();
  }

  private String normalizeUserId(String userId) {
    return userId == null ? "" : userId.trim();
  }

  private String requireNonBlank(String value, String fieldName) {
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException(fieldName + " must not be blank");
    }
    return value.trim();
  }
}
