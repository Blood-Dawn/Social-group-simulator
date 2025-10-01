package org.campusboard.sgs.view;

import javax.swing.*;
import java.awt.*;
import org.campusboard.sgs.controller.Controller;
import org.campusboard.sgs.controller.EventBus;
import org.campusboard.sgs.model.Category;

/**
 * Sidebar panel for navigation and filtering.
 * Shows: Categories, Quick Actions, Campus-specific sections
 */
public class SidebarPanel extends JPanel {
    private Controller controller;
    private EventBus eventBus;
    
    private JList<Category> categoriesList;
    private JButton allPostsButton;
    private JButton announcementsButton;
    private JButton eventsButton;
    private JButton clubsButton;
    private JButton clearFiltersButton;
    
    public SidebarPanel(Controller controller, EventBus eventBus) {
        this.controller = controller;
        this.eventBus = eventBus;
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
    }
    
    private void initializeComponents() {
        // TODO: Create "All Posts" button
        // TODO: Create categories list with all Category values
        // TODO: Create quick action buttons for campus features
        // TODO: Create "Clear Filters" button
        // TODO: Set preferred sizes and styling
    }
    
    private void setupLayout() {
        // TODO: Use BoxLayout (vertical)
        // TODO: Add "Navigation" title
        // TODO: Add all posts button
        // TODO: Add "Categories" section with list
        // TODO: Add "Campus" section with quick buttons
        // TODO: Add clear filters at bottom
    }
    
    private void setupEventListeners() {
        // TODO: Add categories list selection listener
        // TODO: Add quick action button listeners
        // TODO: categoriesList.addListSelectionListener(e -> onCategorySelected());
        // TODO: Subscribe to FILTER_CHANGED to update UI
    }
    
    private void onCategorySelected() {
        // TODO: Get selected category
        // TODO: Call controller.applyFilter(category)
    }
    
    private void onAllPostsClicked() {
        // TODO: Call controller.clearFilters()
        // TODO: Clear list selection
    }
    
    private void onAnnouncementsClicked() {
        // TODO: Call controller.applyFilter(Category.ANNOUNCEMENTS)
    }
    
    private void onEventsClicked() {
        // TODO: Call controller.applyFilter(Category.EVENTS)
    }
    
    private void onClubsClicked() {
        // TODO: Call controller.applyFilter(Category.CLUBS_ORGS)
    }
    
    private void onClearFiltersClicked() {
        // TODO: Call controller.clearFilters()
        // TODO: Clear all selections
    }
}
