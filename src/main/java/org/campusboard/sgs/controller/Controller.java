package org.campusboard.sgs.controller;

import org.campusboard.sgs.model.*;
import org.campusboard.sgs.Persistence.PostRepository;
import org.campusboard.sgs.Persistence.UserRepository;
import java.util.UUID;
import java.util.List;

/**
 * Main controller for the campus board application.
 * Coordinates between the model, view, and persistence layers.
 */
public class Controller {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private User currentUser;
    private Category activeFilter;
    private String searchQuery = "";

    public Controller(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }
    
    // ============ POST MANAGEMENT ============
    
    public void createPost(String title, String body) {
        createPost(title, body, Category.GENERAL);
    }
    
    public void createPost(String title, String body, Category category) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (body == null || body.trim().isEmpty()) {
            throw new IllegalArgumentException("Body cannot be empty");
        }

        Post post = new Post(title.trim(), body.trim(), category == null ? Category.GENERAL : category);
        if (currentUser != null) {
            post.setAuthor(currentUser.getUsername());
        }

        postRepository.save(post);
        EventBus.publish(AppEvent.POST_CREATED, post);
        EventBus.publish(AppEvent.POSTS_CHANGED);
    }
    
    public void deletePost(UUID postId) {
        if (postId == null) {
            return;
        }

        if (postRepository.delete(postId)) {
            EventBus.publish(AppEvent.POST_DELETED, postId);
            EventBus.publish(AppEvent.POSTS_CHANGED);
        }
    }
    
    public void likePost(UUID postId) {
        if (postId == null) {
            return;
        }

        Post post = postRepository.likePost(postId);
        if (post != null) {
            EventBus.publish(AppEvent.POST_LIKED, post);
            EventBus.publish(AppEvent.POSTS_CHANGED);
        }
    }
    
    public void dislikePost(UUID postId) {
        if (postId == null) {
            return;
        }

        Post post = postRepository.dislikePost(postId);
        if (post != null) {
            EventBus.publish(AppEvent.POST_DISLIKED, post);
            EventBus.publish(AppEvent.POSTS_CHANGED);
        }
    }
    
    public List<Post> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        posts.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));

        if (activeFilter != null) {
            posts.removeIf(post -> post.getCategory() != activeFilter);
        }

        if (searchQuery != null && !searchQuery.isBlank()) {
            String query = searchQuery.toLowerCase();
            posts.removeIf(post -> !containsIgnoreCase(post.getTitle(), query)
                    && !containsIgnoreCase(post.getBody(), query));
        }

        return posts;
    }
    
    public List<Post> getPostsByCategory(Category category) {
        if (category == null) {
            return getAllPosts();
        }

        List<Post> posts = postRepository.findAll();
        posts.removeIf(post -> post.getCategory() != category);
        posts.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        return posts;
    }
    
    // ============ USER MANAGEMENT ============
    
    public void createUser(String username, String email, String displayName, UserType userType) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User(username.trim(), email.trim(), displayName, userType);
        userRepository.save(user);
        EventBus.publish(AppEvent.DATA_LOADED, user);
    }
    
    public boolean authenticateUser(String username, String password) {
        // Placeholder authentication: match by username only.
        return userRepository.findByUsername(username)
                .map(user -> {
                    setCurrentUser(user);
                    return true;
                })
                .orElse(false);
    }
    
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null) {
            EventBus.publish(AppEvent.USER_LOGGED_IN, user);
        }
    }

    public void logout() {
        if (currentUser != null) {
            currentUser = null;
            EventBus.publish(AppEvent.USER_LOGGED_OUT);
        }
    }

    public void performSearch(String query) {
    searchQuery = query == null ? "" : query.trim().toLowerCase();
        EventBus.publish(AppEvent.SEARCH_REQUESTED, searchQuery);
        EventBus.publish(AppEvent.POSTS_CHANGED);
    }
    
    // ============ SEARCH & FILTER ============
    
    public List<Post> searchPosts(String query) {
        performSearch(query);
        return getAllPosts();
    }
    
    public void applyFilter(Category category) {
        activeFilter = category;
        EventBus.publish(AppEvent.FILTER_CHANGED, category);
        EventBus.publish(AppEvent.POSTS_CHANGED);
    }
    
    public void clearFilters() {
        activeFilter = null;
        searchQuery = "";
        EventBus.publish(AppEvent.FILTER_CHANGED, null);
        EventBus.publish(AppEvent.SEARCH_REQUESTED, "");
        EventBus.publish(AppEvent.POSTS_CHANGED);
    }
    
    // ============ CAMPUS-SPECIFIC FEATURES ============
    
    public List<Post> getAnnouncementsPosts() {
        return getPostsByCategory(Category.ANNOUNCEMENTS);
    }
    
    public List<Post> getEventsPosts() {
        return getPostsByCategory(Category.EVENTS);
    }
    
    public List<Post> getClubsPosts() {
        return getPostsByCategory(Category.CLUBS_ORGS);
    }

    private boolean containsIgnoreCase(String value, String query) {
        return value != null && value.toLowerCase().contains(query);
    }
}
