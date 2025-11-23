package org.campusboard.sgs.view.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingConstants;
import org.campusboard.sgs.controller.CommentController;
import org.campusboard.sgs.model.Comment;
import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.util.Session;

public final class PostDetailDialog extends JDialog {
  private final Post post;
  private final CommentController comments;
  private final Session session;
  private final JPanel commentsPanel = new JPanel();
  private final JTextArea addArea = new JTextArea(3, 40);

  private PostDetailDialog(JFrame owner, Post post, CommentController comments, Session session) {
    super(owner, "Post Details", true);
    this.post = post;
    this.comments = comments;
    this.session = session;
    buildUI();
    refreshComments();
    pack();
    setLocationRelativeTo(owner);
    setMinimumSize(new Dimension(520, 400));
  }

  public static void showDialog(JFrame owner, Post post, CommentController comments, Session session) {
    var dlg = new PostDetailDialog(owner, post, comments, session);
    dlg.setVisible(true);
  }

  private void buildUI() {
    setLayout(new BorderLayout(10, 10));
    add(createHeader(), BorderLayout.NORTH);
    add(createBody(), BorderLayout.CENTER);
    add(createAddCommentPanel(), BorderLayout.SOUTH);
  }

  private JPanel createHeader() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

    JLabel title = new JLabel(post.title(), SwingConstants.CENTER);
    title.setFont(new Font("Arial", Font.BOLD, 18));
    title.setAlignmentX(CENTER_ALIGNMENT);
    JLabel meta = new JLabel("@" + post.author() + " • " + formatTimestamp(post.createdAt()) + " • " + post.category());
    meta.setFont(new Font("Arial", Font.PLAIN, 12));

    panel.add(title);
    panel.add(Box.createVerticalStrut(4));
    panel.add(meta);
    return panel;
  }

  private JScrollPane createBody() {
    commentsPanel.setLayout(new BoxLayout(commentsPanel, BoxLayout.Y_AXIS));
    commentsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JPanel container = new JPanel();
    container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

    JTextArea body = new JTextArea(post.body());
    body.setWrapStyleWord(true);
    body.setLineWrap(true);
    body.setEditable(false);
    body.setFont(new Font("Arial", Font.PLAIN, 14));
    body.setOpaque(false);
    body.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
    body.setAlignmentX(LEFT_ALIGNMENT);
    container.add(body);
    container.add(Box.createVerticalStrut(12));

    JLabel commentsLabel = new JLabel("Comments");
    commentsLabel.setFont(new Font("Arial", Font.BOLD, 14));
    commentsLabel.setAlignmentX(LEFT_ALIGNMENT);

    container.add(commentsLabel);
    container.add(Box.createVerticalStrut(6));
    container.add(commentsPanel);

    JScrollPane scroll = new JScrollPane(container);
    scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    return scroll;
  }

  private JPanel createAddCommentPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout(5, 5));
    panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

    addArea.setLineWrap(true);
    addArea.setWrapStyleWord(true);
    addArea.setFont(new Font("Arial", Font.PLAIN, 13));
    addArea.setBorder(BorderFactory.createLineBorder(new java.awt.Color(200, 200, 200)));

    JButton addButton = new JButton("Add Comment");
    addButton.addActionListener(e -> handleAddComment());

    panel.add(addArea, BorderLayout.CENTER);
    panel.add(addButton, BorderLayout.EAST);
    return panel;
  }

  private void refreshComments() {
    commentsPanel.removeAll();
    List<Comment> list = comments.listForPost(post.id());
    if (list.isEmpty()) {
      JLabel none = new JLabel("No comments yet.");
      none.setFont(new Font("Arial", Font.ITALIC, 12));
      commentsPanel.add(none);
    } else {
      for (Comment c : list) {
        commentsPanel.add(renderComment(c));
        commentsPanel.add(Box.createVerticalStrut(8));
      }
    }
    commentsPanel.revalidate();
    commentsPanel.repaint();
  }

  private JPanel renderComment(Comment c) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new java.awt.Color(230, 230, 230)),
        BorderFactory.createEmptyBorder(6, 8, 6, 8)));

    JLabel meta = new JLabel("@" + c.author() + " • " + formatTimestamp(c.createdAt()));
    meta.setFont(new Font("Arial", Font.BOLD, 12));

    JLabel body = new JLabel("<html>" + c.body().replace("\n", "<br>") + "</html>");
    body.setFont(new Font("Arial", Font.PLAIN, 12));

    panel.add(meta);
    panel.add(Box.createVerticalStrut(3));
    panel.add(body);
    return panel;
  }

  private void handleAddComment() {
    String text = addArea.getText().trim();
    if (text.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Comment cannot be empty.", "Validation", JOptionPane.WARNING_MESSAGE);
      return;
    }
    boolean ok = comments.addComment(post.id(), text);
    if (ok) {
      addArea.setText("");
      refreshComments();
    } else if (!session.isAuthenticated()) {
      JOptionPane.showMessageDialog(this, "Please login to comment.", "Login required", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  private String formatTimestamp(LocalDateTime createdAt) {
    if (createdAt == null) {
      return "Unknown";
    }
    Duration duration = Duration.between(createdAt, LocalDateTime.now());
    long minutes = duration.toMinutes();
    long hours = duration.toHours();
    long days = duration.toDays();
    if (minutes < 1) return "Just now";
    if (minutes < 60) return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
    if (hours < 24) return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
    if (days < 7) return days + " day" + (days > 1 ? "s" : "") + " ago";
    return createdAt.format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
  }
}
