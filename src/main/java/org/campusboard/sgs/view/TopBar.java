package org.campusboard.sgs.view;

import javax.swing.*;
import java.awt.*;
import org.campusboard.sgs.controller.Controller;
import org.campusboard.sgs.controller.EventBus;

/**
 * Top navigation bar for the campus board.
 * Contains: Logo, Search, User Actions, Create Post button
 */
public class TopBar extends JPanel {
    private Controller controller;
    private EventBus eventBus;
    
    private JTextField searchField;
    private JButton createPostButton;
    private JLabel userInfoLabel;
    private JButton loginButton;
    private JButton logoutButton;
    
    public TopBar(Controller controller, EventBus eventBus) {
        this.controller = controller;
        this.eventBus = eventBus;
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
    }
    
    private void initializeComponents() {
        // TODO: Create search field with placeholder text
        // TODO: Create "Create Post" button
        // TODO: Create user info label (shows current user)
        // TODO: Create login/logout buttons
        // TODO: Set preferred sizes and styling
    }
    
    private void setupLayout() {
        // TODO: Use FlowLayout or BoxLayout
        // TODO: Add logo/title on left
        // TODO: Add search field in center
        // TODO: Add user controls on right
        // TODO: Set background color and borders
    }
    
    private void setupEventListeners() {
        // TODO: Add search field listener (publish SEARCH_CHANGED)
        // TODO: Add create post button listener (show CreatePostDialog)
        // TODO: Add login button listener
        // TODO: Add logout button listener
        // TODO: Subscribe to USER_LOGGED_IN/OUT events
    }
    
    private void onSearchChanged() {
        // TODO: Get search text and call controller.searchPosts()
    }
    
    private void onCreatePostClicked() {
        // TODO: Show CreatePostDialog
        // TODO: Pass controller for post creation
    }
    
    private void onLoginClicked() {
        // TODO: Show LoginDialog
    }
    
    private void onLogoutClicked() {
        // TODO: Call controller.logoutUser()
    }
    
    public void updateUserInfo(String username, String userType) {
        // TODO: Update user info label with current user
        // TODO: Show/hide login/logout buttons appropriately
    }
}
