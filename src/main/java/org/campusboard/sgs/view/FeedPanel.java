package org.campusboard.sgs.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import org.campusboard.sgs.controller.Controller;
import org.campusboard.sgs.controller.EventBus;
import org.campusboard.sgs.controller.AppEvent;
import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.model.Category;

/**
 * Feed panel - Displays scrollable list of posts with filtering
 */
public class FeedPanel extends JPanel {
    private Controller controller;
    private EventBus eventBus;
    
    private JPanel postsContainer;
    private JScrollPane scrollPane;
    private Category currentFilter = null;
    private String currentSearchQuery = "";
    
    public FeedPanel(Controller controller, EventBus eventBus) {
        this.controller = controller;
        this.eventBus = eventBus;
        
        System.out.println("📋 FeedPanel: Initializing feed panel...");
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        
        loadInitialPosts();
    }
    
    /**
     * Create scrollable container with modern styling
     */
    private void initializeComponents() {
        System.out.println("📋 FeedPanel: Creating components...");
        
        postsContainer = new JPanel();
        postsContainer.setLayout(new BoxLayout(postsContainer, BoxLayout.Y_AXIS));
        postsContainer.setBackground(new Color(248, 249, 250));
        postsContainer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        scrollPane = new JScrollPane(postsContainer);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }
    
    /**
     * Simple BorderLayout setup
     */
    private void setupLayout() {
        System.out.println("📋 FeedPanel: Setting up layout...");
        
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    /**
     * Subscribe to 3 events: POSTS_CHANGED, FILTER_CHANGED, SEARCH_REQUESTED
     * All use thread-safe UI updates
     */
    private void setupEventListeners() {
        System.out.println("📋 FeedPanel: Setting up event listeners...");
        
        try {
            // When posts change, refresh feed
            EventBus.subscribe(AppEvent.POSTS_CHANGED, data -> {
                SwingUtilities.invokeLater(() -> {
                    System.out.println("📋 FeedPanel: Posts changed! Refreshing feed...");
                    refreshPosts();
                });
            });
            
            // When category filter changes, update and refresh
            EventBus.subscribe(AppEvent.FILTER_CHANGED, data -> {
                SwingUtilities.invokeLater(() -> {
                    System.out.println("📋 FeedPanel: Filter changed! Updating feed...");
                    if (data instanceof Category) {
                        currentFilter = (Category) data;
                    } else {
                        currentFilter = null;
                    }
                    refreshPosts();
                });
            });
            
            // When search requested, update query and refresh
            EventBus.subscribe(AppEvent.SEARCH_REQUESTED, data -> {
                SwingUtilities.invokeLater(() -> {
                    System.out.println("📋 FeedPanel: Search requested: " + data);
                    if (data instanceof String) {
                        currentSearchQuery = (String) data;
                    } else {
                        currentSearchQuery = "";
                    }
                    refreshPosts();
                });
            });
            
        } catch (Exception e) {
            System.err.println("⚠️ FeedPanel: Error setting up event listeners: " + e.getMessage());
        }
    }
    
    /**
     * Public method to refresh posts (called by MainWindow)
     */
    public void refreshPosts() {
        System.out.println("📋 FeedPanel: Refreshing posts...");
        loadInitialPosts();
    }
    
    /**
     * Load and display posts with current filters applied
     * Shows empty state if no posts
     */
    private void loadInitialPosts() {
        System.out.println("📋 FeedPanel: Loading initial posts...");
        
        try {
            postsContainer.removeAll();
            
            List<Post> posts = controller.getAllPosts();
            
            // Empty state
            if (posts == null || posts.isEmpty()) {
                System.out.println("📋 FeedPanel: No posts to display");
                JLabel emptyLabel = new JLabel("No posts yet. Click 'New Post' to create one!");
                emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                emptyLabel.setForeground(Color.GRAY);
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                postsContainer.add(Box.createVerticalStrut(50));
                postsContainer.add(emptyLabel);
                postsContainer.revalidate();
                postsContainer.repaint();
                return;
            }
            
            // Apply filters (category + search)
            List<Post> filteredPosts = filterPosts(posts);
            
            System.out.println("📋 FeedPanel: Showing " + filteredPosts.size() + " posts");
            
            // Create PostCard for each post
            for (Post post : filteredPosts) {
                try {
                    System.out.println("  📄 Creating card for: " + post.getTitle());
                    PostCard card = new PostCard(post, controller);
                    
                    card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
                    card.setAlignmentX(Component.LEFT_ALIGNMENT);
                    
                    postsContainer.add(card);
                    postsContainer.add(Box.createVerticalStrut(20));
                    
                } catch (Exception e) {
                    System.err.println("⚠️ FeedPanel: Error creating PostCard: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            // Refresh UI
            postsContainer.revalidate();
            postsContainer.repaint();
            scrollPane.revalidate();
            scrollPane.repaint();
            
            // Scroll to top
            SwingUtilities.invokeLater(() -> {
                scrollPane.getVerticalScrollBar().setValue(0);
            });
            
            System.out.println("✅ FeedPanel: Feed refresh complete!");
            
        } catch (Exception e) {
            System.err.println("⚠️ FeedPanel: Error loading posts: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Filter posts by category and search query
     * Both filters can be active at the same time
     */
    private List<Post> filterPosts(List<Post> posts) {
        List<Post> filtered = new ArrayList<>(posts);
        
        // Filter by category
        if (currentFilter != null) {
            filtered.removeIf(post -> post.getCategory() != currentFilter);
            System.out.println("📋 FeedPanel: Filtered by category: " + currentFilter + " (" + filtered.size() + " posts)");
        }
        
        // Filter by search query (title and body)
        if (currentSearchQuery != null && !currentSearchQuery.trim().isEmpty()) {
            String query = currentSearchQuery.toLowerCase();
            filtered.removeIf(post -> 
                !post.getTitle().toLowerCase().contains(query) && 
                !post.getBody().toLowerCase().contains(query)
            );
            System.out.println("📋 FeedPanel: Filtered by search: '" + query + "' (" + filtered.size() + " posts)");
        }
        
        return filtered;
    }
}