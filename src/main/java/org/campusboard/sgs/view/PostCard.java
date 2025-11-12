package org.campusboard.sgs.view;

import org.campusboard.sgs.controller.PostController;
import org.campusboard.sgs.model.*;
import org.campusboard.sgs.util.Session;
import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

public class PostCard extends JPanel {
  private final PostController controller;
  private final Session session;
  private Post post;

  private JLabel titleLabel;
  private JTextArea bodyArea;
  private JLabel authorLabel;
  private JLabel timestampLabel;
  private JLabel categoryBadge;
  private JButton likeButton;
  private JButton deleteButton;

  private static final Color FAU_NAVY = new Color(0, 51, 102);
  private static final Color FAU_RED = new Color(206, 17, 65);
  private static final Color CARD_BG = Color.WHITE;
  private static final Color HOVER_BG = new Color(248, 249, 250);

  public PostCard(PostController controller, Session session) {
    this.controller = controller;
    this.session = session;
    setLayout(new BorderLayout(10, 10));
    setBackground(CARD_BG);
    setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
      BorderFactory.createEmptyBorder(15, 15, 15, 15)
    ));

    addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent e) { setBackground(HOVER_BG); }
      public void mouseExited(java.awt.event.MouseEvent e) { setBackground(CARD_BG); }
    });

    add(createHeader(), BorderLayout.NORTH);
    add(createContent(), BorderLayout.CENTER);
    add(createFooter(), BorderLayout.SOUTH);
  }

  public void bind(Post post) {
    this.post = post;
    titleLabel.setText(post.title());
    bodyArea.setText(post.body());
    authorLabel.setText("@" + post.author());
    timestampLabel.setText(formatTime(post.createdAt()));
    categoryBadge.setText(formatCategory(post.category()));
    likeButton.setText("♥ " + post.likeCount());

    // Update delete button visibility based on centralized permission check
    deleteButton.setVisible(controller.canModifyPost(post));
  }

  private JPanel createHeader() {
    JPanel header = new JPanel(new BorderLayout());
    header.setOpaque(false);

    JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    left.setOpaque(false);

    authorLabel = new JLabel("@author");
    authorLabel.setFont(new Font("Arial", Font.BOLD, 13));
    authorLabel.setForeground(FAU_NAVY);

    timestampLabel = new JLabel("Just now");
    timestampLabel.setFont(new Font("Arial", Font.PLAIN, 11));
    timestampLabel.setForeground(Color.GRAY);

    left.add(authorLabel);
    left.add(Box.createHorizontalStrut(5));
    left.add(timestampLabel);

    categoryBadge = new JLabel("Category");
    categoryBadge.setFont(new Font("Arial", Font.BOLD, 10));
    categoryBadge.setForeground(FAU_NAVY);
    categoryBadge.setOpaque(true);
    categoryBadge.setBackground(new Color(225, 239, 254));
    categoryBadge.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

    header.add(left, BorderLayout.WEST);
    header.add(categoryBadge, BorderLayout.EAST);

    return header;
  }

  private JPanel createContent() {
    JPanel content = new JPanel();
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.setOpaque(false);
    content.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

    titleLabel = new JLabel("Title");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setForeground(FAU_NAVY);
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

    bodyArea = new JTextArea("Body content");
    bodyArea.setFont(new Font("Arial", Font.PLAIN, 13));
    bodyArea.setForeground(new Color(60, 60, 60));
    bodyArea.setLineWrap(true);
    bodyArea.setWrapStyleWord(true);
    bodyArea.setEditable(false);
    bodyArea.setOpaque(false);
    bodyArea.setBorder(null);
    bodyArea.setAlignmentX(Component.LEFT_ALIGNMENT);

    content.add(titleLabel);
    content.add(Box.createVerticalStrut(8));
    content.add(bodyArea);

    return content;
  }

  private JPanel createFooter() {
    JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
    footer.setOpaque(false);

    // TODO: Replace emoji with proper icon resource (Task 8: Icon Assets)
    likeButton = new JButton("♥ 0");
    likeButton.setFont(new Font("Arial", Font.PLAIN, 13));
    likeButton.setForeground(Color.GRAY);
    likeButton.setBackground(CARD_BG);
    likeButton.setBorderPainted(false);
    likeButton.setFocusPainted(false);
    likeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    likeButton.addActionListener(e -> controller.toggleLike(post));

    likeButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent e) { likeButton.setForeground(FAU_RED); }
      public void mouseExited(java.awt.event.MouseEvent e) { likeButton.setForeground(Color.GRAY); }
    });

    // TODO: Replace emoji with proper icon resource (Task 8: Icon Assets)
    deleteButton = new JButton("🗑 Delete");
    deleteButton.setFont(new Font("Arial", Font.PLAIN, 12));
    deleteButton.setForeground(FAU_RED);
    deleteButton.setBackground(CARD_BG);
    deleteButton.setBorderPainted(true);
    deleteButton.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(FAU_RED, 1),
        BorderFactory.createEmptyBorder(3, 8, 3, 8)
    ));
    deleteButton.setFocusPainted(false);
    deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    deleteButton.addActionListener(e -> handleDelete());

    deleteButton.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent e) {
        deleteButton.setBackground(new Color(255, 240, 240));
      }
      public void mouseExited(java.awt.event.MouseEvent e) {
        deleteButton.setBackground(CARD_BG);
      }
    });

    footer.add(likeButton);
    footer.add(Box.createHorizontalStrut(10));
    footer.add(deleteButton);

    return footer;
  }

  private void handleDelete() {
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete this post?\n\"" + post.title() + "\"",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
    );

    if (confirm == JOptionPane.YES_OPTION) {
      controller.delete(post);
    }
  }

  private String formatTime(Instant time) {
    if (time == null) return "Unknown";
    Duration dur = Duration.between(time, Instant.now());
    long mins = dur.toMinutes();
    long hours = dur.toHours();
    long days = dur.toDays();

    if (mins < 1) return "Just now";
    if (mins < 60) return mins + "m ago";
    if (hours < 24) return hours + "h ago";
    if (days < 7) return days + "d ago";
    return days / 7 + "w ago";
  }

  private String formatCategory(Category cat) {
    if (cat == null) return "General";
    return switch (cat) {
      case ANNOUNCEMENTS -> "Announcements";
      case STUDY_GROUPS -> "Study Groups";
      case EVENTS -> "Events";
      case LOST_FOUND -> "Lost & Found";
    };
  }
}
