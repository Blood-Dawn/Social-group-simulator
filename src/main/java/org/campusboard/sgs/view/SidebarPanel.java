package org.campusboard.sgs.view;

import org.campusboard.sgs.controller.PostController;
import org.campusboard.sgs.filter.*;
import org.campusboard.sgs.model.*;
import org.campusboard.sgs.util.*;
import javax.swing.*;
import java.awt.*;

public class SidebarPanel extends JPanel {
  private final PostController controller;
  private final EventBus bus;
  private final Session session;
  private JLabel userLabel;
  private final ImageIcon adminRoleIcon = IconLoader.loadOrPlaceholder("admin", "moderate", 16, FAU_NAVY);

  private static final Color FAU_NAVY = new Color(0, 51, 102);
  private static final Color SIDEBAR_BG = new Color(245, 245, 245);
  private static final Color BUTTON_BG = Color.WHITE;
  private static final Color BUTTON_HOVER = new Color(225, 239, 254);
  private static final int FILTER_ICON_SIZE = 20;

  public SidebarPanel(PostController controller, EventBus bus, Session session) {
    this.controller = controller;
    this.bus = bus;
    this.session = session;

    setLayout(new BorderLayout());
    setBackground(SIDEBAR_BG);
    setPreferredSize(new Dimension(260, 0));

    JPanel filterPanel = createFilterPanel();
    JScrollPane scrollPane = new JScrollPane(filterPanel);
    scrollPane.setBorder(null);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    add(scrollPane, BorderLayout.CENTER);
    add(createUserPanel(), BorderLayout.SOUTH);

    // Subscribe to login events to update user label
    bus.subscribe(Events.USER_LOGGED_IN, e -> updateUserLabel());
    bus.subscribe(Events.USER_LOGGED_OUT, e -> updateUserLabel());

    updateUserLabel();
  }

  private JPanel createFilterPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(SIDEBAR_BG);
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Title
    JLabel title = new JLabel("Filters");
    title.setFont(new Font("Arial", Font.BOLD, 16));
    title.setForeground(FAU_NAVY);
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(title);
    panel.add(Box.createVerticalStrut(10));

    // All Posts
    panel.add(createFilterButton("All Posts", new AllFilter(), loadCategoryIcon("all")));
    panel.add(Box.createVerticalStrut(5));

    // Category filters
    panel.add(createFilterButton("Announcements", new CategoryFilter(Category.ANNOUNCEMENTS),
        loadCategoryIcon("announcements")));
    panel.add(Box.createVerticalStrut(5));
    panel.add(createFilterButton("Study Groups", new CategoryFilter(Category.STUDY_GROUPS),
        loadCategoryIcon("study-groups")));
    panel.add(Box.createVerticalStrut(5));
    panel.add(createFilterButton("Events", new CategoryFilter(Category.EVENTS),
        loadCategoryIcon("events")));
    panel.add(Box.createVerticalStrut(5));
    panel.add(createFilterButton("Lost & Found", new CategoryFilter(Category.LOST_FOUND),
        loadCategoryIcon("lost-found")));
    panel.add(Box.createVerticalStrut(5));

    // Trending
    panel.add(Box.createVerticalStrut(10));
    panel.add(createFilterButton("Trending", new TrendingFilter(),
        loadCategoryIcon("trending")));
    panel.add(Box.createVerticalStrut(5));

    return panel;
  }

  private JButton createFilterButton(String text, FilterStrategy filter, ImageIcon icon) {
    JButton button = new JButton(text);
    button.setAlignmentX(Component.LEFT_ALIGNMENT);
    button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
    button.setBackground(BUTTON_BG);
    button.setForeground(FAU_NAVY);
    button.setFont(new Font("Arial", Font.PLAIN, 13));
    button.setHorizontalAlignment(SwingConstants.LEFT);
    if (icon != null) {
      button.setIcon(icon);
      button.setIconTextGap(10);
    }
    button.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    button.setFocusPainted(false);
    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    button.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent e) {
        button.setBackground(BUTTON_HOVER);
      }

      public void mouseExited(java.awt.event.MouseEvent e) {
        button.setBackground(BUTTON_BG);
      }
    });

    button.addActionListener(e -> controller.setFilter(filter));

    return button;
  }

  private ImageIcon loadCategoryIcon(String name) {
    return IconLoader.loadOrPlaceholder("categories", name, FILTER_ICON_SIZE, FAU_NAVY);
  }

  private JPanel createUserPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    panel.setBackground(SIDEBAR_BG);
    panel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)));

    userLabel = new JLabel();
    userLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    userLabel.setForeground(Color.GRAY);
    userLabel.setVerticalAlignment(SwingConstants.TOP);

    panel.add(userLabel, BorderLayout.CENTER);

    return panel;
  }

  private void updateUserLabel() {
    SwingUtilities.invokeLater(() -> {
      if (session.isAuthenticated()) {
        User user = session.user();
        boolean admin = user.role() == Role.ADMIN && adminRoleIcon != null;
        userLabel.setIcon(admin ? adminRoleIcon : null);
        userLabel.setIconTextGap(8);
        if (admin) {
          userLabel.setText("<html><b>Logged in as:</b><br>" +
              user.username() + "<br><span style='color:#666;'>Administrator</span></html>");
          userLabel.setToolTipText("Administrator privileges enabled");
        } else {
          userLabel.setText("<html><b>Logged in as:</b><br>" +
              user.username() + " (" + user.role() + ")</html>");
          userLabel.setToolTipText(null);
        }
      } else {
        userLabel.setText("<html><b>Guest mode</b><br>" +
            "Login to access<br>full features</html>");
        userLabel.setIcon(null);
        userLabel.setToolTipText(null);
      }
    });
  }

  public EventBus getEventBus() {
    return bus;
  }
}
