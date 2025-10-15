package org.campusboard.sgs.view;

import javax.swing.*;
import java.awt.*;
import org.campusboard.sgs.controller.Controller;
import org.campusboard.sgs.controller.EventBus;
import org.campusboard.sgs.controller.AppEvent;
import org.campusboard.sgs.model.UserType;
import org.campusboard.sgs.model.Category;

/**
 * Left sidebar with category filters and navigation
 */
public class SidebarPanel extends JPanel {
    private Controller controller;

    private JButton currentSelection = null;
    private JPanel mainContainer;
    private JPanel adminPanel;
    private JLabel guestInfoLabel;
    private UserType currentRole = UserType.GUEST;

    private static final Color FAU_RED = new Color(206, 17, 65);
    private static final Color SELECTION_BLUE = new Color(0, 120, 215);
    private static final Color ADMIN_PANEL_BG = new Color(255, 246, 224);
    private static final Color ADMIN_BORDER = new Color(255, 214, 102);
    
    public SidebarPanel(Controller controller) {
        this.controller = controller;
        
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(240, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
        
        mainContainer = new JPanel();
        mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBackground(Color.WHITE);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        
        // NAVIGATION
        addSection(mainContainer, "NAVIGATION");
        addButton(mainContainer, "📁 All Posts", null);
        mainContainer.add(Box.createVerticalStrut(20));
        
        // QUICK ACCESS
        addSection(mainContainer, "QUICK ACCESS");
        addButton(mainContainer, "📢 Announcements", "ANNOUNCEMENTS");
        addButton(mainContainer, "📅 Events", "EVENTS");
        addButton(mainContainer, "📚 Study Groups", "STUDY_GROUPS");
        mainContainer.add(Box.createVerticalStrut(20));
        
        // ALL CATEGORIES
        addSection(mainContainer, "ALL CATEGORIES");
        addButton(mainContainer, "Announcements", "ANNOUNCEMENTS");
        addButton(mainContainer, "Events", "EVENTS");
        addButton(mainContainer, "Study groups", "STUDY_GROUPS");
        addButton(mainContainer, "Housing", "HOUSING");
        addButton(mainContainer, "Academics", "ACADEMICS");

        mainContainer.add(Box.createVerticalStrut(20));

        adminPanel = createAdminPanel();
        mainContainer.add(adminPanel);

        guestInfoLabel = new JLabel("Guest mode: login to access posting and moderation tools.");
        guestInfoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        guestInfoLabel.setForeground(Color.GRAY);
        guestInfoLabel.setAlignmentX(LEFT_ALIGNMENT);
        guestInfoLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        mainContainer.add(guestInfoLabel);

        mainContainer.add(Box.createVerticalGlue());
        
        // CLEAR FILTERS
        JButton clear = new JButton("✖️ Clear Filters");
        clear.setFont(new Font("Arial", Font.BOLD, 13));
        clear.setForeground(FAU_RED);
        clear.setBackground(Color.WHITE);
        clear.setBorderPainted(false);
        clear.setFocusPainted(false);
        clear.setHorizontalAlignment(SwingConstants.LEFT);
        clear.setAlignmentX(LEFT_ALIGNMENT);
        clear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clear.addActionListener(e -> controller.clearFilters());
        mainContainer.add(clear);

        JScrollPane scroll = new JScrollPane(mainContainer);
        scroll.setBorder(null);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scroll, BorderLayout.CENTER);

        applyRoleStyling(currentRole);
        setupEvents();
    }
    
    /**
     * Add section header
     */
    private void addSection(JPanel parent, String title) {
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Arial", Font.BOLD, 11));
        lbl.setForeground(Color.GRAY);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        parent.add(lbl);
        parent.add(Box.createVerticalStrut(10));
    }
    
    /**
     * Add category filter button with selection tracking and hover effect
     */
    private void addButton(JPanel parent, String text, String categoryName) {
        JButton btn = new JButton(text);
        
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.DARK_GRAY);
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        
        // Click handler - applies filter and updates selection
        btn.addActionListener(e -> {
            // Deselect previous
            if (currentSelection != null) {
                currentSelection.setBackground(Color.WHITE);
                currentSelection.setForeground(Color.DARK_GRAY);
                currentSelection.setFont(new Font("Arial", Font.PLAIN, 13));
            }
            
            // Select this one - blue background
            currentSelection = btn;
            btn.setBackground(SELECTION_BLUE);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 13));
            
            // Apply filter
            if (categoryName == null) {
                controller.clearFilters();
            } else {
                try {
                    Category cat = Category.valueOf(categoryName);
                    controller.applyFilter(cat);
                } catch (Exception ex) {
                    System.err.println("⚠️ Category not found: " + categoryName);
                }
            }
        });
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn != currentSelection) {
                    btn.setBackground(new Color(240, 240, 240));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn != currentSelection) {
                    btn.setBackground(Color.WHITE);
                }
            }
        });
        
        parent.add(btn);
        parent.add(Box.createVerticalStrut(5));
    }
    
    /**
     * Subscribe to EventBus events
     */
    private void setupEvents() {
        try {
            EventBus.subscribe(AppEvent.FILTER_CHANGED, data -> {
                // Events handled by button clicks
            });
            EventBus.subscribe(AppEvent.USER_ROLE_CHANGED, data -> {
                SwingUtilities.invokeLater(() -> {
                    UserType role = data instanceof UserType ? (UserType) data : UserType.GUEST;
                    applyRoleStyling(role);
                });
            });
        } catch (Exception e) {
            System.err.println("⚠️ Error subscribing to events: " + e.getMessage());
        }
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, ADMIN_BORDER),
                BorderFactory.createEmptyBorder(12, 10, 12, 10)));
        panel.setBackground(Color.WHITE);

        JLabel header = new JLabel("ADMIN TOOLS");
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setForeground(new Color(140, 90, 0));
        header.setAlignmentX(LEFT_ALIGNMENT);
        panel.add(header);
        panel.add(Box.createVerticalStrut(10));

        panel.add(createAdminButton("🛡 Manage Users"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(createAdminButton("🧹 Moderate Posts"));
        panel.add(Box.createVerticalStrut(8));
        panel.add(createAdminButton("📊 View Reports"));

        panel.setVisible(false);
        return panel;
    }

    private JButton createAdminButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(new Color(90, 60, 0));
        button.setBackground(new Color(255, 239, 204));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ADMIN_BORDER, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> System.out.println("🛠 Admin action triggered: " + text));
        return button;
    }

    private void applyRoleStyling(UserType role) {
        currentRole = role == null ? UserType.GUEST : role;
        boolean isAdmin = currentRole == UserType.STAFF || currentRole == UserType.ADMINISTRATION;
        boolean isGuest = currentRole == UserType.GUEST;

        adminPanel.setVisible(isAdmin);
        guestInfoLabel.setVisible(isGuest);

        if (isAdmin) {
            adminPanel.setBackground(ADMIN_PANEL_BG);
            mainContainer.setBackground(new Color(255, 250, 235));
        } else {
            adminPanel.setBackground(Color.WHITE);
            mainContainer.setBackground(Color.WHITE);
        }

        revalidate();
        repaint();
    }
}
