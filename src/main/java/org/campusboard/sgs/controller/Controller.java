package org.campusboard.sgs.controller;

import org.campusboard.sgs.model.*;
import org.campusboard.sgs.Persistence.PostRepository;
import org.campusboard.sgs.Persistence.UserRepository;
import org.campusboard.sgs.filter.FilterStrategy;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.UnaryOperator;

/**
 * Main controller for the campus board application.
 * Coordinates between the model, view, and persistence layers.
 */
public class Controller {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UndoManager undoManager;
    private User currentUser;
    // Persisted guest account ensures anonymous posts still resolve to a concrete
    // user.
    private final User guestUser;
    private Category activeFilter;
    private FilterStrategy filterStrategy;
    private String searchQuery = "";

    /**
     * 2024-05-29 Update: unified guest attribution string so posts created
     * without an authenticated account land in the feed as "@guest" instead of
     * the old "@unknown" placeholder.
     */
    private static final String DEFAULT_GUEST_HANDLE = Post.GUEST_AUTHOR_FALLBACK;

    public Controller(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.undoManager = new UndoManager();
        this.guestUser = initializeGuestUser();
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

        // Always attach either the logged-in user or the shared guest account when
        // persisting posts.
        Post post = new Post(title.trim(), body.trim(), category, resolveAuthor());

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

    public AuthenticationResult authenticateUser(String username, char[] password) {
        if (username == null || username.isBlank()) {
            return AuthenticationResult.failure("Username is required.");
        }
        if (password == null || password.length == 0) {
            return AuthenticationResult.failure("Password is required.");
        }

        char[] passwordCopy = Arrays.copyOf(password, password.length);
        try {
            return userRepository.findByUsername(username.trim())
                    .map(user -> {
                        boolean valid = userRepository.validatePassword(user, passwordCopy);
                        if (valid) {
                            user.resetFailedLoginAttempts();
                            user.setLastLoginAt(LocalDateTime.now());
                            userRepository.update(user);
                            setCurrentUser(user);
                            return AuthenticationResult.success("Welcome back, " + user.getDisplayName() + "!");
                        }

                        user.incrementFailedLoginAttempts();
                        userRepository.update(user);
                        return AuthenticationResult
                                .failure("Invalid username or password. Attempts: " + user.getFailedLoginAttempts());
                    })
                    .orElse(AuthenticationResult.failure("Invalid username or password."));
        } finally {
            Arrays.fill(passwordCopy, '\0');
        }
    }

    public static class AuthenticationResult {
        private final boolean success;
        private final String message;

        private AuthenticationResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public static AuthenticationResult success(String message) {
            return new AuthenticationResult(true, message);
        }

        public static AuthenticationResult failure(String message) {
            return new AuthenticationResult(false, message);
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public UserType getCurrentUserType() {
        return currentUser == null ? UserType.GUEST : currentUser.getUserType();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        EventBus.publish(AppEvent.USER_ROLE_CHANGED, getCurrentUserType());
        if (user != null) {
            EventBus.publish(AppEvent.USER_LOGGED_IN, user);
        }
    }

    public void logout() {
        if (currentUser != null) {
            setCurrentUser(null);
            EventBus.publish(AppEvent.USER_LOGGED_OUT);
        }
    }

    private User initializeGuestUser() {
        User guest = userRepository.findByUsername("guest")
                .orElseGet(() -> userRepository.save(
                        new User("guest", "guest@campusboard.local", "Guest", UserType.GUEST)));

        if (guest.getPasswordHash() == null) {
            userRepository.assignPassword(guest, "guest123".toCharArray());
        }
        return guest;
    }

    private User resolveAuthor() {
        // Fall back to the guest user so repository writes never encounter null
        // authors.
        return currentUser != null ? currentUser : guestUser;
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

    // ============ UNDO/REDO SUPPORT ============

    /**
     * Create a post with undo support
     */
    public void createPostWithUndo(String title, String body, Category category) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (body == null || body.trim().isEmpty()) {
            throw new IllegalArgumentException("Body cannot be empty");
        }

        CreatePostCommand cmd = new CreatePostCommand(postRepository, title.trim(), body.trim(), category,
                resolveAuthor());
        try {
            undoManager.doCommand(cmd);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create post", e);
        }
    }

    /**
     * Edit a post with undo support
     */
    public void editPostWithUndo(UUID postId, UnaryOperator<Post> updater) {
        if (postId == null) {
            throw new IllegalArgumentException("Post ID cannot be null");
        }

        EditPostCommand cmd = new EditPostCommand(postRepository, postId, updater);
        try {
            undoManager.doCommand(cmd);
        } catch (Exception e) {
            throw new RuntimeException("Failed to edit post", e);
        }
    }

    /**
     * Delete a post with undo support
     */
    public void deletePostWithUndo(UUID postId) {
        if (postId == null) {
            throw new IllegalArgumentException("Post ID cannot be null");
        }

        DeletePostCommand cmd = new DeletePostCommand(postRepository, postId);
        try {
            undoManager.doCommand(cmd);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete post", e);
        }
    }

    /**
     * Like a post with undo support
     */
    public void likePostWithUndo(UUID postId) {
        if (postId == null) {
            throw new IllegalArgumentException("Post ID cannot be null");
        }

        LikePostCommand cmd = new LikePostCommand(postRepository, postId);
        try {
            undoManager.doCommand(cmd);
        } catch (Exception e) {
            throw new RuntimeException("Failed to like post", e);
        }
    }

    /**
     * Undo the last command
     */
    public void undo() {
        try {
            undoManager.undo();
        } catch (Exception e) {
            throw new RuntimeException("Failed to undo", e);
        }
    }

    /**
     * Redo the last undone command
     */
    public void redo() {
        try {
            undoManager.redo();
        } catch (Exception e) {
            throw new RuntimeException("Failed to redo", e);
        }
    }

    /**
     * Check if undo is available
     */
    public boolean canUndo() {
        return undoManager.canUndo();
    }

    /**
     * Check if redo is available
     */
    public boolean canRedo() {
        return undoManager.canRedo();
    }

    // ============ FILTER STRATEGY SUPPORT ============

    /**
     * Set the filter strategy
     */
    public void setFilter(FilterStrategy strategy) {
        this.filterStrategy = strategy;
        EventBus.publish(AppEvent.FILTER_CHANGED, strategy);
        EventBus.publish(AppEvent.POSTS_CHANGED);
    }

    /**
     * Get filtered posts using the current FilterStrategy and search query
     */
    public List<Post> getFiltered() {
        List<Post> posts = postRepository.findAll();
        posts.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));

        // Apply filter strategy if set
        if (filterStrategy != null) {
            posts = filterStrategy.apply(posts);
        }

        // Apply search query
        if (searchQuery != null && !searchQuery.isBlank()) {
            String query = searchQuery.toLowerCase();
            posts.removeIf(post -> !containsIgnoreCase(post.getTitle(), query)
                    && !containsIgnoreCase(post.getBody(), query));
        }

        return posts;
    }

    /**
     * Get the EventBus instance for subscriptions
     */
    public EventBus getBus() {
        return new EventBus();
    }
}
