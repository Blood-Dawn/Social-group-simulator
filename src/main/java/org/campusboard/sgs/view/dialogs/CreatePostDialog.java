package org.campusboard.sgs.view.dialogs;

import javax.swing.*;
import java.awt.*;
import org.campusboard.sgs.controller.Controller;
import org.campusboard.sgs.model.Category;

/**
 * Dialog for creating new posts.
 * Shows: Title field, Body area, Category dropdown, Submit/Cancel buttons
 */
public class CreatePostDialog extends JDialog {
    private Controller controller;
    
    private JTextField titleField;
    private JTextArea bodyArea;
    private JComboBox<Category> categoryCombo;
    private JButton submitButton;
    private JButton cancelButton;
    
    public CreatePostDialog(JFrame parent, Controller controller) {
        super(parent, "Create New Post", true);
        this.controller = controller;
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        setSize(500, 400);
        setLocationRelativeTo(parent);
    }
    
    private void initializeComponents() {
        // TODO: Create title text field with character limit
        // TODO: Create body text area with scroll pane and character limit
        // TODO: Create category dropdown with all categories
        // TODO: Create submit and cancel buttons
        // TODO: Set default category to GENERAL
    }
    
    private void setupLayout() {
        // TODO: Use BorderLayout
        // TODO: Create form panel with labels and fields
        // TODO: Add buttons panel at bottom
        // TODO: Add proper spacing and margins
    }
    
    private void setupEventListeners() {
        // TODO: Add submit button listener
        // TODO: Add cancel button listener
        // TODO: Add escape key listener to cancel
        // TODO: Add validation on submit
    }
    
    private void onSubmit() {
        // TODO: Validate title and body not empty
        // TODO: Validate title/body length limits
        // TODO: Call controller.createPost(title, body, category)
        // TODO: Close dialog on success
        // TODO: Show error message on validation failure
    }
    
    private void onCancel() {
        // TODO: Close dialog without saving
    }
}