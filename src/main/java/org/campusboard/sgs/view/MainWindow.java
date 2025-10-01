package org.campusboard.sgs.view;

import javax.swing.*;
import java.awt.*;
import org.campusboard.sgs.controller.Controller;
import org.campusboard.sgs.controller.EventBus;

/**
 * Main application window for the campus board.
 * Layout: TopBar + (SidebarPanel | FeedPanel)
 */
public class MainWindow extends JFrame {
    private Controller controller;
    private EventBus eventBus;
    private TopBar topBar;
    private SidebarPanel sidebarPanel;
    private FeedPanel feedPanel;
    
    public MainWindow(Controller controller, EventBus eventBus) {
        super("Campus Board - Social Group Simulator");
        this.controller = controller;
        this.eventBus = eventBus;
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void initializeComponents() {
        // TODO: Initialize all UI components
        // TODO: topBar = new TopBar(controller, eventBus);
        // TODO: sidebarPanel = new SidebarPanel(controller, eventBus);
        // TODO: feedPanel = new FeedPanel(controller, eventBus);
    }
    
    private void setupLayout() {
        // TODO: Set layout manager (BorderLayout recommended)
        // TODO: Add topBar to NORTH
        // TODO: Add sidebarPanel to WEST
        // TODO: Add feedPanel to CENTER
        // TODO: Set proper sizes and borders
    }
    
    private void setupEventListeners() {
        // TODO: Subscribe to relevant AppEvents
        // TODO: eventBus.subscribe(AppEvent.POSTS_CHANGED, this::refreshFeed);
        // TODO: eventBus.subscribe(AppEvent.USER_LOGGED_IN, this::updateUserInfo);
        // TODO: eventBus.subscribe(AppEvent.USER_LOGGED_OUT, this::showLoginPrompt);
    }
    
    private void refreshFeed() {
        // TODO: Refresh the feed panel when posts change
    }
    
    private void updateUserInfo() {
        // TODO: Update top bar with current user info
    }
    
    private void showLoginPrompt() {
        // TODO: Show login dialog or redirect to login
    }
}
