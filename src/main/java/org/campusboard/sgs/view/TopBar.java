package org.campusboard.sgs.view;

import org.campusboard.sgs.controller.*;
import org.campusboard.sgs.model.Role;
import org.campusboard.sgs.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class TopBar extends JPanel {
  private final PostController posts;
  private final AuthController auth;
  private final EventBus bus;
  private final Session session;

  private JTextField searchField;
  private JButton loginButton;
  private JLabel usernameLabel;
  private JLabel roleBadgeLabel;
  private JButton logoutButton;
  private JPanel authPanel;
  private final ImageIcon adminBadgeIcon = IconLoader.loadOrPlaceholder("admin", "moderate", 16, FAU_RED);
  private final ImageIcon loginIcon = IconLoader.loadAction("login", 20);
  private final ImageIcon logoutIcon = IconLoader.loadAction("logout", 20);

  private static final Color FAU_NAVY = new Color(0, 51, 102);
  private static final Color FAU_RED = new Color(206, 17, 65);
  private static final Color TOPBAR_BG = Color.WHITE;

  public TopBar(PostController posts, AuthController auth, EventBus bus, Session session) {
    this.posts = posts;
    this.auth = auth;
    this.bus = bus;
    this.session = session;

    setLayout(new BorderLayout(10, 0));
    setBackground(TOPBAR_BG);
    setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createMatteBorder(0, 0, 2, 0, FAU_NAVY),
        BorderFactory.createEmptyBorder(10, 15, 10, 15)));

    add(createLogoPanel(), BorderLayout.WEST);
    add(createSearchPanel(), BorderLayout.CENTER);
    add(createAuthPanel(), BorderLayout.EAST);

    // Subscribe to login/logout events
    bus.subscribe(Events.USER_LOGGED_IN, e -> updateAuthUI());
    bus.subscribe(Events.USER_LOGGED_OUT, e -> updateAuthUI());
    bus.subscribe(Events.SHOW_LOGIN, e -> showLoginDialog());

    updateAuthUI();
  }

  private JPanel createLogoPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    panel.setOpaque(false);

    ImageIcon logoIcon = IconLoader.loadUI("CB_Icon", 32);
    JLabel logoBadge = new JLabel(logoIcon != null ? logoIcon : new ImageIcon());
    logoBadge.setPreferredSize(new Dimension(40, 40));
    logoBadge.setToolTipText("CampusBoard - Social Hub for College Campuses");

    panel.add(logoBadge);
    panel.add(Box.createHorizontalStrut(10));

    // Add app title
    JLabel titleLabel = new JLabel("CampusBoard");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    titleLabel.setForeground(FAU_NAVY);
    panel.add(titleLabel);

    return panel;
  }

  private JPanel createSearchPanel() {
    JPanel panel = new JPanel(new BorderLayout(5, 0));
    panel.setOpaque(false);

    ImageIcon searchIcon = IconLoader.loadOrPlaceholder("ui", "search", 16, FAU_NAVY);
    JLabel searchIconLabel = new JLabel(searchIcon);
    searchIconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));

    searchField = new JTextField();
    searchField.setName("searchField");
    searchField.setFont(new Font("Arial", Font.PLAIN, 14));
    searchField.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    searchField.addActionListener(e -> posts.setSearch(searchField.getText()));

    // Add document listener for real-time search
    searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
      private javax.swing.Timer timer = new javax.swing.Timer(300, e -> {
        posts.setSearch(searchField.getText());
      });

      {
        timer.setRepeats(false);
      }

      @Override
      public void insertUpdate(javax.swing.event.DocumentEvent e) {
        timer.restart();
      }

      @Override
      public void removeUpdate(javax.swing.event.DocumentEvent e) {
        timer.restart();
      }

      @Override
      public void changedUpdate(javax.swing.event.DocumentEvent e) {
        timer.restart();
      }
    });

    panel.add(searchIconLabel, BorderLayout.WEST);
    panel.add(searchField, BorderLayout.CENTER);

    return panel;
  }

  private JPanel createAuthPanel() {
    authPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    authPanel.setOpaque(false);

    // Login button
    loginButton = new JButton(loginIcon != null ? "" : "Login");
    loginButton.setToolTipText("Login");
    loginButton.setIcon(loginIcon);
    loginButton.setPreferredSize(new Dimension(40, 32));
    loginButton.setFont(new Font("Arial", Font.BOLD, 13));
    loginButton.setForeground(Color.WHITE);
    loginButton.setBackground(FAU_RED);
    loginButton.setBorderPainted(false);
    loginButton.setFocusPainted(false);
    loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    loginButton.addActionListener(e -> showLoginDialog());

    // Username label
    usernameLabel = new JLabel();
    usernameLabel.setFont(new Font("Arial", Font.BOLD, 13));
    usernameLabel.setForeground(FAU_NAVY);

    roleBadgeLabel = new JLabel();
    roleBadgeLabel.setFont(new Font("Arial", Font.BOLD, 12));
    roleBadgeLabel.setForeground(FAU_NAVY);

    // Logout button
    logoutButton = new JButton(logoutIcon != null ? "" : "Logout");
    logoutButton.setToolTipText("Logout");
    logoutButton.setIcon(logoutIcon);
    logoutButton.setPreferredSize(new Dimension(40, 32));
    logoutButton.setFont(new Font("Arial", Font.PLAIN, 12));
    logoutButton.setForeground(Color.WHITE);
    logoutButton.setBackground(FAU_NAVY);
    logoutButton.setBorderPainted(true);
    logoutButton.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(FAU_NAVY, 1),
        BorderFactory.createEmptyBorder(5, 15, 5, 15)));
    logoutButton.setFocusPainted(false);
    logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    logoutButton.addActionListener(e -> handleLogout());

    return authPanel;
  }

  private void updateAuthUI() {
    SwingUtilities.invokeLater(() -> {
      authPanel.removeAll();
      roleBadgeLabel.setVisible(false);

      if (session.isAuthenticated()) {
        usernameLabel.setText("Welcome, " + session.user().username());
        configureRoleBadge(session.role());
        authPanel.add(usernameLabel);
        if (roleBadgeLabel.isVisible()) {
          authPanel.add(roleBadgeLabel);
        }
        authPanel.add(logoutButton);
      } else {
        authPanel.add(loginButton);
      }

      authPanel.revalidate();
      authPanel.repaint();
    });
  }

  private void configureRoleBadge(Role role) {
    if (role == null) {
      roleBadgeLabel.setVisible(false);
      return;
    }

    if (role == Role.ADMIN && adminBadgeIcon != null) {
      roleBadgeLabel.setIcon(adminBadgeIcon);
      roleBadgeLabel.setText("");
      roleBadgeLabel.setToolTipText("Administrator privileges enabled");
      roleBadgeLabel.setVisible(true);
    } else {
      roleBadgeLabel.setIcon(null);
      roleBadgeLabel.setText("(" + role.name() + ")");
      roleBadgeLabel.setToolTipText("Role: " + role.name());
      roleBadgeLabel.setVisible(true);
    }
  }

  private void showLoginDialog() {
    JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
    JTextField usernameField = new JTextField(15);
    JPasswordField passwordField = new JPasswordField(15);

    panel.add(new JLabel("Username:"));
    panel.add(usernameField);
    panel.add(new JLabel("Password:"));
    panel.add(passwordField);

    int result = JOptionPane.showConfirmDialog(
        this,
        panel,
        "Login",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      String username = usernameField.getText();
      String password = new String(passwordField.getPassword());

      if (auth.login(username, password)) {
        JOptionPane.showMessageDialog(this,
            "Logged in successfully as " + username,
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(this,
            "Invalid username or password",
            "Login Failed",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  private void handleLogout() {
    int choice = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to logout?",
        "Confirm Logout",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {
      auth.logout();
      JOptionPane.showMessageDialog(this,
          "Logged out successfully",
          "Success",
          JOptionPane.INFORMATION_MESSAGE);
    }
  }

  public EventBus getEventBus() {
    return bus;
  }
}
