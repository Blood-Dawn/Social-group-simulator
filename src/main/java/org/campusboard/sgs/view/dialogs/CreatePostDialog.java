package org.campusboard.sgs.view;

import javax.swing.*;
import java.awt.*;
import org.campusboard.sgs.controller.Controller;
import org.campusboard.sgs.model.Category;

/**
 * Modal dialog for creating new posts
 * Features validation and category selection
 */
public class CreatePostDialog extends JDialog {
    private Controller controller;
    private JTextField titleField;
    private JTextArea bodyArea;
    private JComboBox<String> categoryCombo;
    private JButton submitButton;
    private JButton cancelButton;
    private static final Color FAU_NAVY = new Color(0, 51, 102);
    
    public CreatePostDialog(JFrame parent, Controller controller) {
        super(parent, "Create New Post", true);
        this.controller = controller;
        initializeComponents();
        setupLayout();
        setupEventListeners();
        setSize(600, 500);
        setLocationRelativeTo(parent);
    }
    
    /**
     * Create form fields with styling
     */
    private void initializeComponents() {
        titleField = new JTextField();
        titleField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Body area with word wrap
        bodyArea = new JTextArea(10, 40);
        bodyArea.setFont(new Font("Arial", Font.PLAIN, 14));
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        bodyArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        
        // Category dropdown
        String[] categories = {"Announcements", "Events", "Study Groups", "Lost & Found", "Housing", "Academics", "Clubs & Organizations"};
        categoryCombo = new JComboBox<>(categories);
        categoryCombo.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Submit button - FAU Navy
        submitButton = new JButton("Create Post");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setForeground(Color.WHITE);
        submitButton.setBackground(FAU_NAVY);
        submitButton.setFocusPainted(false);
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Cancel button
        cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Layout form in vertical sections
     */
    private void setupLayout() {
        setLayout(new BorderLayout(20, 20));
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // TITLE section
        JLabel titleLabel = new JLabel("Post Title:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        titleField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        formPanel.add(titleLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(titleField);
        formPanel.add(Box.createVerticalStrut(20));
        
        // BODY section
        JLabel bodyLabel = new JLabel("Post Body:");
        bodyLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bodyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JScrollPane bodyScroll = new JScrollPane(bodyArea);
        bodyScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        bodyScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        formPanel.add(bodyLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(bodyScroll);
        formPanel.add(Box.createVerticalStrut(20));
        
        // CATEGORY section
        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        categoryCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        categoryCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        formPanel.add(categoryLabel);
        formPanel.add(Box.createVerticalStrut(8));
        formPanel.add(categoryCombo);
        
        // BUTTONS panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(cancelButton);
        buttonPanel.add(submitButton);
        
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Set up button listeners and keyboard shortcuts
     */
    private void setupEventListeners() {
        submitButton.addActionListener(e -> handleSubmit());
        cancelButton.addActionListener(e -> dispose());
        titleField.addActionListener(e -> bodyArea.requestFocus()); // Enter key moves to body
    }
    
    /**
     * Validate and submit post
     * Shows error dialogs if validation fails
     */
    private void handleSubmit() {
        String title = titleField.getText().trim();
        String body = bodyArea.getText().trim();
        String categoryStr = (String) categoryCombo.getSelectedItem();
        
        // Validation
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a post title", "Missing Title", JOptionPane.WARNING_MESSAGE);
            titleField.requestFocus();
            return;
        }
        
        if (body.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter post content", "Missing Content", JOptionPane.WARNING_MESSAGE);
            bodyArea.requestFocus();
            return;
        }
        
        // Convert to Category enum and create post
        Category category = convertToCategory(categoryStr);
        controller.createPost(title, body, category);
        System.out.println("✅ CreatePostDialog: Post created successfully!");
        dispose();
    }
    
    /**
     * Convert user-friendly name to Category enum
     */
    private Category convertToCategory(String categoryStr) {
        switch (categoryStr) {
            case "Announcements": return Category.ANNOUNCEMENTS;
            case "Events": return Category.EVENTS;
            case "Study Groups": return Category.STUDY_GROUPS;
            case "Lost & Found": return Category.LOST_AND_FOUND;
            case "Housing": return Category.HOUSING;
            case "Academics": return Category.ACADEMICS;
            case "Clubs & Organizations": return Category.CLUBS_ORGS;
            default: return Category.ANNOUNCEMENTS;
        }
    }
}