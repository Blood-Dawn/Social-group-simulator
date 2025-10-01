package org.campusboard.sgs.view;

import javax.swing.*;
import java.awt.*;
import org.campusboard.sgs.controller.Controller;
import org.campusboard.sgs.model.Post;

/**
 * Individual post card component.
 * Shows: Title, Body, Author, Category, Like/Dislike buttons, Timestamp
 */
public class PostCard extends JPanel {
    private Controller controller;
    private Post post;
    
    private JLabel titleLabel;
    private JTextArea bodyArea;
    private JLabel authorLabel;
    private JLabel categoryLabel;
    private JLabel timestampLabel;
    private JButton likeButton;
    private JButton dislikeButton;
    private JButton deleteButton; // Only show for own posts
    
    public PostCard(Post post, Controller controller) {
        this.post = post;
        this.controller = controller;
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        updateContent();
    }
    
    private void initializeComponents() {
        // TODO: Create title label (bold, larger font)
        // TODO: Create body text area (read-only, word wrap)
        // TODO: Create author label
        // TODO: Create category badge/label
        // TODO: Create timestamp label
        // TODO: Create like/dislike buttons with icons
        // TODO: Create delete button (conditional)
    }
    
    private void setupLayout() {
        // TODO: Use BorderLayout or BoxLayout
        // TODO: Title at top
        // TODO: Body in center
        // TODO: Author, category, timestamp at bottom left
        // TODO: Action buttons at bottom right
        // TODO: Add padding and borders
        // TODO: Set background color
    }
    
    private void setupEventListeners() {
        // TODO: Add like button listener
        // TODO: Add dislike button listener  
        // TODO: Add delete button listener (if applicable)
        // TODO: likeButton.addActionListener(e -> onLikeClicked());
    }
    
    private void updateContent() {
        // TODO: Set title text
        // TODO: Set body text
        // TODO: Set author text
        // TODO: Set category text with styling
        // TODO: Set timestamp (format: "2 hours ago")
        // TODO: Update like/dislike counts on buttons
    }
    
    private void onLikeClicked() {
        // TODO: Call controller.likePost(post.getId())
        // TODO: Update like count display
    }
    
    private void onDislikeClicked() {
        // TODO: Call controller.dislikePost(post.getId())
        // TODO: Update dislike count display
    }
    
    private void onDeleteClicked() {
        // TODO: Show confirmation dialog
        // TODO: Call controller.deletePost(post.getId())
    }
    
    public void refreshLikeCounts() {
        // TODO: Update like/dislike button text with current counts
        // TODO: Call this when post is updated
    }
}
