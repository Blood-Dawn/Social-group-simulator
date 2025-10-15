package org.campusboard.sgs.view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import org.campusboard.sgs.controller.Controller;
import org.campusboard.sgs.controller.EventBus;
import org.campusboard.sgs.controller.AppEvent;
import org.campusboard.sgs.view.dialogs.CreatePostDialog;

/**
 * Top navigation bar with search, create post, and user controls
 */
public class TopBar extends JPanel {
    private Controller controller;
    
    private JTextField searchField;
    private JButton createPostButton;
    private JLabel userAvatarLabel;
    private JLabel userNameLabel;
    private JButton loginButton;
    private JButton logoutButton;

    private ImageIcon guestAvatarIcon;
    private ImageIcon userAvatarIcon;
    private ImageIcon loginDoorIcon;
    private ImageIcon logoutDoorIcon;
    
    private static final Color FAU_NAVY = new Color(0, 51, 102);
    private static final Color FAU_RED = new Color(206, 17, 65);
    private static final Color SEARCH_BG = new Color(245, 245, 245);
    private static final Color SEARCH_BORDER = new Color(220, 220, 220);
    
    public TopBar(Controller controller) {
        this.controller = controller;
        
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
        
        buildIcons();

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
        
        // USER INFO
        userAvatarLabel = new JLabel();
        userAvatarLabel.setHorizontalAlignment(SwingConstants.CENTER);
        userAvatarLabel.setPreferredSize(new Dimension(36, 36));
        userAvatarLabel.setOpaque(false);
        userAvatarLabel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        userNameLabel = new JLabel("Loading...");
        userNameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userNameLabel.setForeground(Color.WHITE);

        // LOGIN BUTTON
        loginButton = new JButton("Login", loginDoorIcon);
        loginButton.setFont(new Font("Arial", Font.PLAIN, 13));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(0, 82, 164));
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        loginButton.setIconTextGap(6);
        loginButton.setMargin(new Insets(6, 12, 6, 14));
        loginButton.setToolTipText("Log in to your Campus Board account");
        loginButton.getAccessibleContext().setAccessibleDescription("Log in to your Campus Board account");
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                loginButton.setBackground(new Color(0, 96, 184));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                loginButton.setBackground(new Color(0, 82, 164));
            }
        });

        // LOGOUT BUTTON
        logoutButton = new JButton("Logout", logoutDoorIcon);
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 13));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(FAU_NAVY);
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        logoutButton.setIconTextGap(6);
        logoutButton.setMargin(new Insets(6, 12, 6, 14));
        logoutButton.setToolTipText("Log out of your Campus Board account");
        logoutButton.getAccessibleContext().setAccessibleDescription("Log out of your Campus Board account");

        // Hover effect
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                logoutButton.setBackground(new Color(0, 70, 140));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                logoutButton.setBackground(FAU_NAVY);
            }
        });

        userNameLabel.getAccessibleContext().setAccessibleDescription("Displays the active account name");
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
        
        // RIGHT - Create Post + User + Auth Buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 8));
        rightPanel.setBackground(FAU_NAVY);

        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        userInfoPanel.setBackground(FAU_NAVY);
        userInfoPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        userInfoPanel.add(userAvatarLabel);
        userInfoPanel.add(userNameLabel);

        rightPanel.add(createPostButton);
        rightPanel.add(userInfoPanel);
        rightPanel.add(loginButton);
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
        
        // LOGIN BUTTON - Prompt for username
        loginButton.addActionListener(e -> {
            System.out.println("🚪 TopBar: Login button clicked");
            String username = JOptionPane.showInputDialog(
                this,
                "Enter username:",
                "Login",
                JOptionPane.PLAIN_MESSAGE
            );

            if (username != null) {
                String trimmed = username.trim();
                if (!trimmed.isEmpty()) {
                    boolean authenticated = controller.authenticateUser(trimmed, "");
                    if (!authenticated) {
                        JOptionPane.showMessageDialog(
                            this,
                            "We couldn't find an account for '" + trimmed + "'.",
                            "Login failed",
                            JOptionPane.WARNING_MESSAGE
                        );
                    }
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Please enter a username to continue.",
                        "Login required",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
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
                userAvatarLabel.setIcon(userAvatarIcon);
                userAvatarLabel.setToolTipText("Authenticated user avatar");
                userAvatarLabel.getAccessibleContext().setAccessibleDescription("Authenticated user avatar");
                userNameLabel.setText(user.getUsername());
                userNameLabel.setToolTipText("Logged in as " + user.getUsername());
                loginButton.setVisible(false);
                logoutButton.setVisible(true);
                System.out.println("🔝 TopBar: Displaying user: " + user.getUsername());
            } else {
                userAvatarLabel.setIcon(guestAvatarIcon);
                userAvatarLabel.setToolTipText("Guest user avatar");
                userAvatarLabel.getAccessibleContext().setAccessibleDescription("Guest user avatar");
                userNameLabel.setText("Guest");
                userNameLabel.setToolTipText("Browsing as guest");
                loginButton.setVisible(true);
                logoutButton.setVisible(false);
                System.out.println("🔝 TopBar: No user logged in");
            }
        } catch (Exception e) {
            System.err.println("⚠️ TopBar: Error updating user display: " + e.getMessage());
            userAvatarLabel.setIcon(guestAvatarIcon);
            userNameLabel.setText("Error");
        }
    }

    private void buildIcons() {
        guestAvatarIcon = createAvatarIcon(
            new Color(235, 240, 245),
            new Color(133, 146, 166),
            new Color(158, 169, 187),
            36
        );
        userAvatarIcon = createAvatarIcon(
            new Color(0, 82, 164),
            Color.WHITE,
            new Color(224, 235, 255),
            36
        );
        loginDoorIcon = createDoorIcon(
            new Color(0, 82, 164),
            new Color(189, 210, 255),
            new Color(224, 235, 255),
            true,
            24
        );
        logoutDoorIcon = createDoorIcon(
            new Color(206, 17, 65),
            new Color(255, 207, 214),
            Color.WHITE,
            false,
            24
        );
    }

    private ImageIcon createAvatarIcon(Color background, Color headColor, Color bodyColor, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int padding = Math.round(size * 0.08f);
            int diameter = size - padding * 2;
            g2.setColor(background);
            g2.fillOval(padding, padding, diameter, diameter);

            int headDiameter = Math.round(size * 0.38f);
            int headX = size / 2 - headDiameter / 2;
            int headY = padding + Math.round(size * 0.10f);
            g2.setColor(headColor);
            g2.fillOval(headX, headY, headDiameter, headDiameter);

            int bodyWidth = Math.round(size * 0.56f);
            int bodyHeight = Math.round(size * 0.32f);
            int bodyX = size / 2 - bodyWidth / 2;
            int bodyY = headY + headDiameter - Math.round(size * 0.08f);
            RoundRectangle2D body = new RoundRectangle2D.Float(
                bodyX,
                bodyY,
                bodyWidth,
                bodyHeight,
                Math.round(size * 0.18f),
                Math.round(size * 0.18f)
            );
            g2.setColor(bodyColor);
            g2.fill(body);

            g2.setColor(new Color(255, 255, 255, 60));
            g2.setStroke(new BasicStroke(Math.max(1, Math.round(size * 0.05f))));
            g2.drawOval(padding, padding, diameter, diameter);
        } finally {
            g2.dispose();
        }

        return new ImageIcon(image);
    }

    private ImageIcon createDoorIcon(
        Color doorColor,
        Color knobColor,
        Color arrowColor,
        boolean entering,
        int size
    ) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int padding = Math.round(size * 0.12f);
            int doorWidth = Math.round(size * 0.58f);
            int doorHeight = Math.round(size * 0.74f);
            int doorX = entering ? padding + Math.round(size * 0.16f) : size - doorWidth - padding;
            int doorY = padding;

            RoundRectangle2D doorShape = new RoundRectangle2D.Float(
                doorX,
                doorY,
                doorWidth,
                doorHeight,
                Math.round(size * 0.2f),
                Math.round(size * 0.2f)
            );
            g2.setColor(doorColor);
            g2.fill(doorShape);

            g2.setColor(new Color(0, 0, 0, 35));
            g2.setStroke(new BasicStroke(Math.max(1, Math.round(size * 0.04f))));
            g2.draw(doorShape);

            int knobDiameter = Math.max(2, Math.round(size * 0.14f));
            int knobX = entering
                ? doorX + Math.round(doorWidth * 0.18f)
                : doorX + doorWidth - Math.round(doorWidth * 0.32f);
            int knobY = doorY + Math.round(doorHeight * 0.52f);
            g2.setColor(knobColor);
            g2.fillOval(knobX, knobY, knobDiameter, knobDiameter);

            int arrowWidth = Math.round(size * 0.40f);
            int arrowHeight = Math.round(size * 0.28f);
            int arrowX = entering ? doorX - Math.round(size * 0.10f) : doorX + doorWidth - arrowWidth + Math.round(size * 0.10f);
            int arrowY = size / 2 - arrowHeight / 2;

            Polygon arrow = new Polygon();
            if (entering) {
                arrow.addPoint(arrowX, arrowY);
                arrow.addPoint(arrowX + arrowWidth, arrowY + arrowHeight / 2);
                arrow.addPoint(arrowX, arrowY + arrowHeight);
            } else {
                arrow.addPoint(arrowX + arrowWidth, arrowY);
                arrow.addPoint(arrowX, arrowY + arrowHeight / 2);
                arrow.addPoint(arrowX + arrowWidth, arrowY + arrowHeight);
            }
            g2.setColor(arrowColor);
            g2.fillPolygon(arrow);
        } finally {
            g2.dispose();
        }

        return new ImageIcon(image);
    }
}
