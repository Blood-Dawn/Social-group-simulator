package org.campusboard.sgs.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import org.campusboard.sgs.controller.Controller;
import org.campusboard.sgs.controller.EventBus;
import org.campusboard.sgs.model.Post;

/**
 * Main feed panel showing posts in scrollable list.
 * Displays posts as PostCard components.
 */
public class FeedPanel extends JPanel {
    private Controller controller;
    private EventBus eventBus;
    
    private JScrollPane scrollPane;
    private JPanel postsContainer;
    private JLabel noPostsLabel;
    
    public FeedPanel(Controller controller, EventBus eventBus) {
        this.controller = controller;
        this.eventBus = eventBus;
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadPosts();
    }
    
    private void initializeComponents() {
        // TODO: Create scrollable container for posts
        // TODO: Create posts container with vertical layout
        // TODO: Create "No posts available" label
        // TODO: Set preferred sizes and styling
    }
    
    private void setupLayout() {
        // TODO: Use BorderLayout
        // TODO: Add scroll pane to CENTER
        // TODO: Add posts container to scroll pane
        // TODO: Set proper margins and padding
    }
    
    private void setupEventListeners() {
        // TODO: Subscribe to POSTS_CHANGED event
        // TODO: Subscribe to FILTER_CHANGED event
        // TODO: Subscribe to SEARCH_CHANGED event
        // TODO: eventBus.subscribe(AppEvent.POSTS_CHANGED, this::refreshPosts);
    }
    
    private void loadPosts() {
        // TODO: Get all posts from controller
        // TODO: Create PostCard for each post
        // TODO: Add to posts container
    }
    
    private void refreshPosts() {
        // TODO: Clear current posts
        // TODO: Reload posts from controller
        // TODO: Update UI
    }
    
    public void showPosts(List<Post> posts) {
        // TODO: Clear posts container
        // TODO: Create PostCard for each post
        // TODO: Add to container
        // TODO: Show/hide no posts label
        // TODO: Repaint panel
    }
    
    public void showFilteredPosts(List<Post> posts) {
        // TODO: Same as showPosts but for filtered results
        // TODO: Maybe add "Filtered results" header
    }
    
    public void showSearchResults(List<Post> posts, String searchQuery) {
        // TODO: Show search results
        // TODO: Add "Search results for: query" header
        // TODO: Highlight search terms in posts
    }
}
