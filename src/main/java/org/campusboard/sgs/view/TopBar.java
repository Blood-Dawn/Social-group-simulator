package org.campusboard.sgs.view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import org.campusboard.sgs.controller.Controller;
import org.campusboard.sgs.controller.EventBus;
import org.campusboard.sgs.controller.AppEvent;
import org.campusboard.sgs.view.CreatePostDialog;

/**
 * Top navigation bar with search, create post, and user controls
 */
public class TopBar extends JPanel {
    private Controller controller;
    private EventBus eventBus;
    
    private JTextField searchField;
    private JButton createPostButton;
    private JLabel userLabel;
    private JButton logoutButton;
    
    private static final Color FAU_NAVY = new Color(0, 51, 102);
    private static final Color FAU_RED = new Color(206, 17, 65);
    private static final Color SEARCH_BG = new Color(245, 245, 245);
    private static final Color SEARCH_BORDER = new Color(220, 220, 220);
    
    public TopBar(Controller controller, EventBus eventBus) {
        this.controller = controller;
        this.eventBus = eventBus;
        
        System.out.println("🔝 TopBar: Initializing top bar...");
        
        createComponents();
        setupLayout();
        setupEventListeners();
        updateUserDisplay();
        
        System.out.println("🔝 TopBar: Initialization complete");
    }
    
    /**
     * Create all UI components with styling
     */
    private void createComponents() {
        System.out.println("🔝 TopBar: Creating components...");
        
        // SEARCH FIELD - Modern light gray style with placeholder
        searchField = new JTextField(35);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBackground(SEARCH_BG);
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SEARCH_BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        searchField.setForeground(Color.DARK_GRAY);
        
        // Placeholder text
        searchField.setText("Search posts...");
        searchField.setForeground(Color.GRAY);
        
        // Focus listener - handles placeholder and border highlight
        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search posts...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.DARK_GRAY);
                }
                // Blue border when focused
                searchField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 120, 215), 2),
                    BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search posts...");
                    searchField.setForeground(Color.GRAY);
                }
                // Gray border when not focused
                searchField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(SEARCH_BORDER, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        // CREATE POST BUTTON - FAU Red
        createPostButton = new JButton("+ New Post");
        createPostButton.setFont(new Font("Arial", Font.BOLD, 13));
        createPostButton.setForeground(Color.WHITE);
        createPostButton.setBackground(FAU_RED);
        createPostButton.setBorderPainted(false);
        createPostButton.setFocusPainted(false);
        createPostButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        createPostButton.setPreferredSize(new Dimension(120, 35));
        
        // Hover effect
        createPostButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                createPostButton.setBackground(new Color(230, 15, 60));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                createPostButton.setBackground(FAU_RED);
            }
        });
        
        // USER LABEL
        userLabel = new JLabel("👤 Loading...");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        
        // LOGOUT BUTTON
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 13));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(FAU_NAVY);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                logoutButton.setBackground(new Color(0, 70, 140));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                logoutButton.setBackground(FAU_NAVY);
            }
        });
    }
    
    /**
     * Arrange components in 3-section layout: [Logo+Title] [Search] [Buttons+User]
     */
    private void setupLayout() {
        System.out.println("🔝 TopBar: Setting up layout...");
        
        setLayout(new BorderLayout());
        setBackground(FAU_NAVY);
        setPreferredSize(new Dimension(0, 70));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // LEFT - Logo + Title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setBackground(FAU_NAVY);
        
        JLabel logo = new JLabel("F");
        logo.setFont(new Font("Arial", Font.BOLD, 28));
        logo.setForeground(Color.WHITE);
        logo.setOpaque(true);
        logo.setBackground(FAU_RED);
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        logo.setPreferredSize(new Dimension(50, 50));
        
        JLabel title = new JLabel("Campus Board");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        
        leftPanel.add(logo);
        leftPanel.add(title);
        
        // CENTER - Search
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        centerPanel.setBackground(FAU_NAVY);
        
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("Arial", Font.PLAIN, 18));
        
        centerPanel.add(searchIcon);
        centerPanel.add(searchField);
        
        // RIGHT - Create Post + User + Logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 8));
        rightPanel.setBackground(FAU_NAVY);
        
        rightPanel.add(createPostButton);
        rightPanel.add(userLabel);
        rightPanel.add(logoutButton);
        
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }
    
    /**
     * Set up event listeners for search, buttons, and EventBus
     */
    private void setupEventListeners() {
        System.out.println("🔝 TopBar: Setting up event listeners...");
        
        // SEARCH - Real-time as you type
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                performSearch();
            }
            public void removeUpdate(DocumentEvent e) {
                performSearch();
            }
            public void insertUpdate(DocumentEvent e) {
                performSearch();
            }
            
            private void performSearch() {
                String query = searchField.getText().trim();
                
                if (query.equals("Search posts...") || query.isEmpty()) {
                    System.out.println("🔍 TopBar: Empty search - clearing filter");
                    controller.clearFilters();
                    return;
                }
                
                System.out.println("🔍 TopBar: Searching for: " + query);
                controller.performSearch(query);
            }
        });
        
        // CREATE POST BUTTON - Opens dialog
        createPostButton.addActionListener(e -> {
            System.out.println("➕ TopBar: Create post button clicked");
            CreatePostDialog dialog = new CreatePostDialog((JFrame) SwingUtilities.getWindowAncestor(this), controller);
            dialog.setVisible(true);
        });
        
        // LOGOUT BUTTON - With confirmation
        logoutButton.addActionListener(e -> {
            System.out.println("🚪 TopBar: Logout button clicked");
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("✅ TopBar: Logout confirmed");
                controller.logout();
            }
        });
        
        // EVENT BUS - Listen for login/logout
        try {
            EventBus.subscribe(AppEvent.USER_LOGGED_IN, data -> {
                SwingUtilities.invokeLater(() -> {
                    System.out.println("🔝 TopBar: User logged in, updating display");
                    updateUserDisplay();
                });
            });
            
            EventBus.subscribe(AppEvent.USER_LOGGED_OUT, data -> {
                SwingUtilities.invokeLater(() -> {
                    System.out.println("🔝 TopBar: User logged out, updating display");
                    updateUserDisplay();
                });
            });
        } catch (Exception e) {
            System.err.println("⚠️ TopBar: Error subscribing to events: " + e.getMessage());
        }
    }
    
    /**
     * Update user display with current user info
     */
    public void updateUserDisplay() {
        System.out.println("🔝 TopBar: Updating user display...");
        
        try {
            var user = controller.getCurrentUser();
            if (user != null) {
                userLabel.setText("👤 " + user.getUsername());
                logoutButton.setVisible(true);
                System.out.println("🔝 TopBar: Displaying user: " + user.getUsername());
            } else {
                userLabel.setText("👤 Guest");
                logoutButton.setVisible(false);
                System.out.println("🔝 TopBar: No user logged in");
            }
        } catch (Exception e) {
            System.err.println("⚠️ TopBar: Error updating user display: " + e.getMessage());
            userLabel.setText("👤 Error");
        }
    }
}