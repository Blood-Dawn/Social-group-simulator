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
import org.campusboard.sgs.model.UserType;

/**
 * Feed panel - Displays scrollable list of posts with filtering
 */
public class FeedPanel extends JPanel {
    private Controller controller;

    private JPanel postsContainer;
    private JScrollPane scrollPane;
    private JPanel bannerPanel;
    private JLabel bannerLabel;
    private Category currentFilter = null;
    private String currentSearchQuery = "";
    private UserType currentRole = UserType.GUEST;

    private static final Color ADMIN_BANNER_BG = new Color(255, 243, 205);
    private static final Color ADMIN_BANNER_TEXT = new Color(120, 70, 0);
    private static final Color GUEST_BANNER_BG = new Color(227, 242, 253);
    private static final Color GUEST_BANNER_TEXT = new Color(33, 89, 150);
    private static final Color DEFAULT_BANNER_BG = new Color(232, 248, 245);
    private static final Color DEFAULT_BANNER_TEXT = new Color(20, 92, 68);

    public FeedPanel(Controller controller) {
        this.controller = controller;

        System.out.println("📋 FeedPanel: Initializing feed panel...");

        initializeComponents();
        setupLayout();
        setupEventListeners();
        applyRoleStyling(controller.getCurrentUserType());

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

        bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        bannerLabel = new JLabel("Guest mode: sign in to participate in the community.");
        bannerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bannerPanel.add(bannerLabel, BorderLayout.WEST);
    }

    /**
     * Simple BorderLayout setup
     */
    private void setupLayout() {
        System.out.println("📋 FeedPanel: Setting up layout...");

        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));
        add(bannerPanel, BorderLayout.NORTH);
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
            EventBus.subscribe(AppEvent.POSTS_CHANGED, payload -> {
                SwingUtilities.invokeLater(() -> {
                    System.out.println("📋 FeedPanel: Posts changed! Refreshing feed...");
                    refreshPosts();
                });
            });
            
            // When category filter changes, update and refresh
            EventBus.subscribe(AppEvent.FILTER_CHANGED, payload -> {
                SwingUtilities.invokeLater(() -> {
                    System.out.println("📋 FeedPanel: Filter changed! Updating feed...");
                    currentFilter = payload instanceof Category ? (Category) payload : null;
                    refreshPosts();
                });
            });
            
            // When search requested, update query and refresh
            try {
                AppEvent searchEvent = AppEvent.valueOf("SEARCH_REQUESTED");
                EventBus.subscribe(searchEvent, payload -> {
                    SwingUtilities.invokeLater(() -> {
                        System.out.println("📋 FeedPanel: Search requested");
                        currentSearchQuery = payload instanceof String ? (String) payload : "";
                        refreshPosts();
                    });
                });
            } catch (IllegalArgumentException e) {
                System.err.println("⚠️ FeedPanel: AppEvent 'SEARCH_REQUESTED' not found; search events will be ignored.");
            }

            EventBus.subscribe(AppEvent.USER_ROLE_CHANGED, payload -> {
                SwingUtilities.invokeLater(() -> {
                    UserType role = payload instanceof UserType ? (UserType) payload : UserType.GUEST;
                    applyRoleStyling(role);
                    refreshPosts();
                });
            });
        } catch (Exception e) {
            System.err.println("⚠️ FeedPanel: Error setting up event listeners: " + e.getMessage());
            e.printStackTrace();
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
                String emptyMessage = currentRole == UserType.GUEST
                        ? "No posts yet. Sign in to start the conversation!"
                        : "No posts yet. Click 'New Post' to create one!";
                JLabel emptyLabel = new JLabel(emptyMessage);
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
                    card.applyRole(controller.getCurrentUserType());

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
            System.out.println(
                    "📋 FeedPanel: Filtered by category: " + currentFilter + " (" + filtered.size() + " posts)");
        }

        // Filter by search query (title and body)
        if (currentSearchQuery != null && !currentSearchQuery.trim().isEmpty()) {
            String query = currentSearchQuery.toLowerCase();
            filtered.removeIf(post -> !post.getTitle().toLowerCase().contains(query) &&
                    !post.getBody().toLowerCase().contains(query));
            System.out.println("📋 FeedPanel: Filtered by search: '" + query + "' (" + filtered.size() + " posts)");
        }

        return filtered;
    }

    private void applyRoleStyling(UserType role) {
        currentRole = role == null ? UserType.GUEST : role;
        boolean isAdmin = currentRole == UserType.STAFF || currentRole == UserType.ADMINISTRATION;
        boolean isGuest = currentRole == UserType.GUEST;

        if (isAdmin) {
            bannerPanel.setBackground(ADMIN_BANNER_BG);
            bannerLabel.setText("Admin mode: moderation controls are enabled across the feed.");
            bannerLabel.setForeground(ADMIN_BANNER_TEXT);
            postsContainer.setBackground(new Color(254, 248, 236));
        } else if (isGuest) {
            bannerPanel.setBackground(GUEST_BANNER_BG);
            bannerLabel.setText("Guest mode: sign in to create, like, or moderate posts.");
            bannerLabel.setForeground(GUEST_BANNER_TEXT);
            postsContainer.setBackground(new Color(248, 249, 250));
        } else {
            bannerPanel.setBackground(DEFAULT_BANNER_BG);
            bannerLabel.setText("Community mode: enjoy posting and engaging with classmates.");
            bannerLabel.setForeground(DEFAULT_BANNER_TEXT);
            postsContainer.setBackground(new Color(243, 250, 247));
        }

        bannerPanel.setVisible(true);
        revalidate();
        repaint();
    }
}
