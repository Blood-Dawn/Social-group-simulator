package org.campusboard.sgs.view;

import javax.swing.*;
import java.awt.*;
import org.campusboard.sgs.controller.Controller;
import org.campusboard.sgs.controller.AppEvent;
import org.campusboard.sgs.controller.EventBus;

/**
 * Main application window - assembles all components together
 * Layout: TopBar (NORTH) + SidebarPanel (WEST) + FeedPanel (CENTER)
 */
public class MainWindow extends JFrame {
    private Controller controller;

    private TopBar topBar;
    private SidebarPanel sidebarPanel;
    private FeedPanel feedPanel;

    private static final Color FAU_NAVY = new Color(0, 51, 102);

    public MainWindow(Controller controller) {
        super("Campus Board - FAU");
        this.controller = controller;
        
        System.out.println("🏠 MainWindow: Initializing main window...");
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);  // Center on screen
        
        System.out.println("✅ MainWindow: Initialization complete");
    }
    
    /**
     * Create all child components (TopBar, Sidebar, Feed)
     */
    private void initializeComponents() {
        System.out.println("🏠 MainWindow: Creating components...");
        
        topBar = new TopBar(controller);
        sidebarPanel = new SidebarPanel(controller);
        feedPanel = new FeedPanel(controller);
    }
    
    /**
     * Arrange components in BorderLayout
     */
    private void setupLayout() {
        System.out.println("🏠 MainWindow: Setting up layout...");
        
        setLayout(new BorderLayout());
        
        // Use FAU_NAVY as the background for the main content pane so the constant is utilized
        getContentPane().setBackground(FAU_NAVY);
        
        add(topBar, BorderLayout.NORTH);
        add(sidebarPanel, BorderLayout.WEST);
        add(feedPanel, BorderLayout.CENTER);
    }
    
    /**
     * Subscribe to EventBus events for real-time updates
     * Uses SwingUtilities.invokeLater for thread-safe UI updates
     */
    private void setupEventListeners() {
        System.out.println("🏠 MainWindow: Setting up event listeners...");
        
        try {
            // When posts change, refresh the feed
            EventBus.subscribe(AppEvent.POSTS_CHANGED, payload -> {
                SwingUtilities.invokeLater(() -> {
                    System.out.println("🏠 MainWindow: Posts changed, refreshing feed");
                    refreshFeed();
                });
            });
            
            // When user logs in, update display
            EventBus.subscribe(AppEvent.USER_LOGGED_IN, payload -> {
                SwingUtilities.invokeLater(() -> {
                    System.out.println("🏠 MainWindow: User logged in, updating UI");
                    updateUserInfo();
                });
            });
            
            // When user logs out, show login prompt
            EventBus.subscribe(AppEvent.USER_LOGGED_OUT, payload -> {
                SwingUtilities.invokeLater(() -> {
                    System.out.println("🏠 MainWindow: User logged out, showing login prompt");
                    showLoginPrompt();
                });
            });
            
        } catch (Exception e) {
            System.err.println("⚠️ MainWindow: Error setting up event listeners: " + e.getMessage());
        }
    }
    
    /**
     * Refresh the feed panel (delegates to FeedPanel)
     */
    public void refreshFeed() {
        System.out.println("🏠 MainWindow: Refreshing feed...");
        feedPanel.refreshPosts();
    }
    
    /**
     * Update user info display (delegates to TopBar)
     */
    public void updateUserInfo() {
        System.out.println("🏠 MainWindow: Updating user info...");
        topBar.updateUserDisplay();
    }
    
    /**
     * Show simple login prompt
     */
    private void showLoginPrompt() {
        System.out.println("🏠 MainWindow: Showing simple login prompt");
        String username = JOptionPane.showInputDialog(this, "Enter username:");
        if (username != null && !username.trim().isEmpty()) {
            // controller.loginUser(username, "password"); // Method not implemented yet
        }
    }
}