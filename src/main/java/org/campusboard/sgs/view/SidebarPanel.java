package org.campusboard.sgs.view;

import javax.swing.*;
import java.awt.*;
import org.campusboard.sgs.controller.Controller;
import org.campusboard.sgs.controller.EventBus;
import org.campusboard.sgs.controller.AppEvent;
import org.campusboard.sgs.model.Category;

/**
 * Left sidebar with category filters and navigation
 */
public class SidebarPanel extends JPanel {
    private Controller controller;
    
    private JButton currentSelection = null;
    private static final Color FAU_RED = new Color(206, 17, 65);
    private static final Color SELECTION_BLUE = new Color(0, 120, 215);
    
    public SidebarPanel(Controller controller) {
        this.controller = controller;
        
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(240, 0));
        setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY));
        
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setBackground(Color.WHITE);
        main.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        
        // NAVIGATION
        addSection(main, "NAVIGATION");
        addButton(main, "📁 All Posts", null);
        main.add(Box.createVerticalStrut(20));
        
        // QUICK ACCESS
        addSection(main, "QUICK ACCESS");
        addButton(main, "📢 Announcements", "ANNOUNCEMENTS");
        addButton(main, "📅 Events", "EVENTS");
        addButton(main, "📚 Study Groups", "STUDY_GROUPS");
        main.add(Box.createVerticalStrut(20));
        
        // ALL CATEGORIES
        addSection(main, "ALL CATEGORIES");
        addButton(main, "Announcements", "ANNOUNCEMENTS");
        addButton(main, "Events", "EVENTS");
        addButton(main, "Study groups", "STUDY_GROUPS");
        addButton(main, "Housing", "HOUSING");
        addButton(main, "Academics", "ACADEMICS");
        
        main.add(Box.createVerticalGlue());
        
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
        main.add(clear);
        
        JScrollPane scroll = new JScrollPane(main);
        scroll.setBorder(null);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        add(scroll, BorderLayout.CENTER);
        
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
        } catch (Exception e) {
            System.err.println("⚠️ Error subscribing to events: " + e.getMessage());
        }
    }
}