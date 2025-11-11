package org.campusboard.sgs.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a post in the social group simulator.
 * Contains information such as title, body, likes, dislikes, and a unique
 * identifier.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {
    public static final String GUEST_AUTHOR_FALLBACK = "guest";
    private static final String FALLBACK_EMAIL_DOMAIN = "guest.campusboard.local";
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

    @JsonCreator
    public Post(
            @JsonProperty("id") UUID id,
            @JsonProperty("title") String title,
            @JsonProperty("body") String body,
            @JsonProperty("category") Category category,
            @JsonProperty("likes") int likes,
            @JsonProperty("dislikes") int dislikes,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("author") Object author) {
        this.title = title;
        this.body = body;
        this.id = id == null ? UUID.randomUUID() : id;
        this.category = category == null ? Category.GENERAL : category;
        this.likes = likes;
        this.dislikes = dislikes;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
        this.author = resolveAuthor(author);
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public UUID getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getAuthor() {
        return author;
    }

    public int getDislikes() {
        return dislikes;
    }

    public int getLikes() {
        return likes;
    }

    // Setters
    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setAuthor(User author) {
        // Setter also defends against null assignments to keep ownership intact.
        this.author = author != null ? author : createGuestUser(GUEST_AUTHOR_FALLBACK);
    }

    private static User resolveAuthor(Object rawAuthor) {
        if (rawAuthor instanceof User) {
            return (User) rawAuthor;
        }
        if (rawAuthor instanceof Map<?, ?> map) {
            return convertMapToUser(map);
        }
        if (rawAuthor instanceof String handle && !handle.isBlank()) {
            return createGuestUser(handle.trim());
        }
        return createGuestUser(GUEST_AUTHOR_FALLBACK);
    }

    private static User convertMapToUser(Map<?, ?> authorMap) {
        String username = optionalString(authorMap.get("username"));
        String email = optionalString(authorMap.get("email"));
        String displayName = optionalString(authorMap.get("displayName"));
        UserType type = resolveUserType(authorMap.get("userType"));

        String sanitizedUsername = sanitizeUsername(username);
        String resolvedEmail = email != null ? email : sanitizedUsername + "@" + FALLBACK_EMAIL_DOMAIN;
        String resolvedDisplayName = displayName != null ? displayName : capitalizeHandle(sanitizedUsername);

        User user = new User(sanitizedUsername, resolvedEmail, resolvedDisplayName, type);
        return user;
    }

    private static UserType resolveUserType(Object rawType) {
        if (rawType instanceof UserType) {
            return (UserType) rawType;
        }
        if (rawType instanceof String typeName && !typeName.isBlank()) {
            try {
                return UserType.valueOf(typeName.trim().toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ignored) {
                return UserType.GUEST;
            }
        }
        return UserType.GUEST;
    }

    private static String optionalString(Object value) {
        if (value == null) {
            return null;
        }
        String str = value.toString().trim();
        return str.isEmpty() ? null : str;
    }

    private static String sanitizeUsername(String rawUsername) {
        if (rawUsername == null || rawUsername.isBlank()) {
            return GUEST_AUTHOR_FALLBACK;
        }
        return rawUsername.trim().toLowerCase(Locale.ROOT);
    }

    private static String capitalizeHandle(String username) {
        if (username == null || username.isBlank()) {
            return "Guest";
        }
        if (username.length() == 1) {
            return username.toUpperCase(Locale.ROOT);
        }
        return Character.toUpperCase(username.charAt(0)) + username.substring(1);
    }

    private static User createGuestUser(String handle) {
        String sanitized = sanitizeUsername(handle);
        String email = sanitized + "@" + FALLBACK_EMAIL_DOMAIN;
        String displayName = capitalizeHandle(sanitized);
        return new User(sanitized, email, displayName, UserType.GUEST);
    }
}
