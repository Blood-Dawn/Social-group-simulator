package org.campusboard.sgs.view;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import org.campusboard.sgs.controller.PostController;
import org.campusboard.sgs.controller.CommentController;
import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.util.IconLoader;
import org.campusboard.sgs.util.Session;
import org.campusboard.sgs.view.dialogs.PostDetailDialog;
import org.campusboard.sgs.repo.UserRepository;

/**
 * Individual post card that can be re-bound without recreating to avoid feed jumps.
 */
public class PostCard extends JPanel {
  private final PostController controller;
  private final CommentController commentController;
  private final UserRepository userRepo;
  private final Session session;

  private Post post;
  private final JLabel titleLabel = new JLabel();
  private final JLabel authorLabel = new JLabel();
  private final JLabel timestampLabel = new JLabel();
  private final JLabel categoryBadge = new JLabel();
  private final JTextArea bodyArea = new JTextArea();
  private final JButton likeButton = new JButton();
  private final JButton openButton = new JButton("Open");
  private final JButton deleteButton = new JButton("Delete");
  private final JLabel avatarLabel = createAvatarLabel();
  private final ImageIcon deleteIcon = IconLoader.loadAction("delete", 16);
  private final ImageIcon likeOutlineIcon = IconLoader.loadAction("like-outline", 16);
  private final ImageIcon likeFilledIcon = IconLoader.loadAction("like-filled", 16);
  private final ImageIcon likeOutlineGrayIcon;
  private final ImageIcon likeFilledRedIcon;

  private static final Color CARD_BACKGROUND = Color.WHITE;
  private static final Color HOVER_COLOR = new Color(248, 249, 250);
  private static final Color TEXT_PRIMARY = new Color(33, 37, 41);
  private static final Color TEXT_SECONDARY = new Color(108, 117, 125);
  private static final Color FAU_NAVY = new Color(0, 51, 102);
  private static final Color FAU_RED = new Color(206, 17, 65);

  public PostCard(PostController controller, CommentController commentController, Session session, UserRepository userRepo) {
    this.controller = controller;
    this.commentController = commentController;
    this.userRepo = userRepo;
    this.session = session;
    this.likeOutlineGrayIcon = tintIcon(likeOutlineIcon, TEXT_SECONDARY);
    this.likeFilledRedIcon = tintIcon(likeFilledIcon, FAU_RED);
    initialize();
  }

  private void initialize() {
    setLayout(new BorderLayout(12, 10));
    setBackground(CARD_BACKGROUND);
    setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(2, 2, 6, 2),
            BorderFactory.createLineBorder(new Color(0, 0, 0, 20), 1)),
        BorderFactory.createEmptyBorder(16, 16, 16, 16)));

    addMouseListener(new java.awt.event.MouseAdapter() {
      @Override
      public void mouseEntered(java.awt.event.MouseEvent e) {
        setBackground(HOVER_COLOR);
      }

      @Override
      public void mouseExited(java.awt.event.MouseEvent e) {
        setBackground(CARD_BACKGROUND);
      }
    });

    add(createHeader(), BorderLayout.NORTH);
    add(createBody(), BorderLayout.CENTER);
    add(createFooter(), BorderLayout.SOUTH);
  }

  private JPanel createHeader() {
    JPanel header = new JPanel();
    header.setOpaque(false);
    header.setLayout(new BorderLayout(10, 0));

    JPanel left = new JPanel();
    left.setOpaque(false);
    left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));

    left.add(avatarLabel);
    left.add(Box.createHorizontalStrut(10));

    JPanel text = new JPanel();
    text.setOpaque(false);
    text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
    authorLabel.setFont(new Font("Arial", Font.BOLD, 14));
    authorLabel.setForeground(TEXT_PRIMARY);
    timestampLabel.setFont(new Font("Arial", Font.PLAIN, 11));
    timestampLabel.setForeground(TEXT_SECONDARY);
    text.add(authorLabel);
    text.add(timestampLabel);

    left.add(text);

    JPanel right = new JPanel();
    right.setOpaque(false);
    right.setLayout(new BoxLayout(right, BoxLayout.X_AXIS));
    categoryBadge.setFont(new Font("Arial", Font.BOLD, 11));
    categoryBadge.setForeground(FAU_NAVY);
    categoryBadge.setOpaque(true);
    categoryBadge.setBackground(new Color(225, 239, 254));
    categoryBadge.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 230, 255), 1),
        BorderFactory.createEmptyBorder(4, 10, 4, 10)));
    right.add(categoryBadge);

    deleteButton.setIcon(deleteIcon);
    deleteButton.setText(deleteIcon == null ? "Delete" : "");
    deleteButton.setToolTipText("Delete post");
    deleteButton.setFont(new Font("Arial", Font.PLAIN, 12));
    deleteButton.setForeground(FAU_RED);
    deleteButton.setBackground(CARD_BACKGROUND);
    deleteButton.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
    deleteButton.setContentAreaFilled(false);
    deleteButton.setFocusPainted(false);
    deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    deleteButton.addActionListener(e -> handleDelete());
    right.add(Box.createHorizontalStrut(8));
    right.add(deleteButton);

    header.add(left, BorderLayout.WEST);
    header.add(right, BorderLayout.EAST);
    return header;
  }

  private JPanel createBody() {
    JPanel bodyPanel = new JPanel();
    bodyPanel.setOpaque(false);
    bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));

    titleLabel.setFont(emojiCapable(new Font("Arial", Font.BOLD, 18)));
    titleLabel.setForeground(FAU_NAVY);
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

    bodyArea.setFont(emojiCapable(new Font("Arial", Font.PLAIN, 14)));
    bodyArea.setForeground(TEXT_PRIMARY);
    bodyArea.setLineWrap(true);
    bodyArea.setWrapStyleWord(true);
    bodyArea.setEditable(false);
    bodyArea.setOpaque(false);
    bodyArea.setBorder(null);
    bodyArea.setAlignmentX(Component.LEFT_ALIGNMENT);

    bodyPanel.add(titleLabel);
    bodyPanel.add(Box.createVerticalStrut(8));
    bodyPanel.add(bodyArea);
    return bodyPanel;
  }

  private JPanel createFooter() {
    JPanel footer = new JPanel();
    footer.setOpaque(false);
    footer.setLayout(new BoxLayout(footer, BoxLayout.X_AXIS));

    likeButton.setFont(new Font("Arial", Font.PLAIN, 13));
    likeButton.setForeground(TEXT_SECONDARY);
    likeButton.setBackground(CARD_BACKGROUND);
    likeButton.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
    likeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    likeButton.setHorizontalTextPosition(SwingConstants.RIGHT);
    likeButton.setIconTextGap(6);
    likeButton.addActionListener(e -> handleLike());

    footer.add(likeButton);
    footer.add(Box.createHorizontalStrut(8));

    openButton.setFont(new Font("Arial", Font.PLAIN, 12));
    openButton.setForeground(FAU_NAVY);
    openButton.setBackground(CARD_BACKGROUND);
    openButton.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
    openButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    openButton.setToolTipText("Open post details");
    openButton.addActionListener(e -> handleOpen());
    footer.add(openButton);
    return footer;
  }

  private JLabel createAvatarLabel() {
    JLabel avatar = new JLabel("?", JLabel.CENTER) {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillOval(0, 0, getWidth(), getHeight());
        g2.setColor(getForeground());
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
        g2.drawString(getText(), x, y);
        g2.dispose();
      }
    };
    avatar.setPreferredSize(new java.awt.Dimension(36, 36));
    avatar.setFont(new Font("Arial", Font.BOLD, 14));
    avatar.setForeground(Color.WHITE);
    avatar.setBackground(FAU_RED);
    avatar.setOpaque(false);
    avatar.setName("avatar");
    return avatar;
  }

  public void bind(Post post) {
    this.post = post;
    titleLabel.setText(post.title());
    bodyArea.setText(post.body());

    String authorRole = userRepo.find(post.author()).map(u -> " (" + u.role() + ")").orElse("");
    String authorText = "@" + (post.author() == null ? "guest" : post.author()) + authorRole;
    authorLabel.setText(authorText);
    authorLabel.setToolTipText(authorText);
    avatarLabel.setText(extractInitial(post.author()));

    timestampLabel.setText(formatTimestamp(post.createdAt()));
    categoryBadge.setText(formatCategory(post.category().name()));

    updateVisibility();
    updateLikeButton();
  }

  private void updateVisibility() {
    boolean canDelete = controller.canModifyPost(post);
    deleteButton.setVisible(canDelete);
  }

  private void updateLikeButton() {
    boolean liked = session.isAuthenticated() && post.isLikedBy(session.user().username());
    ImageIcon icon = liked ? likeFilledRedIcon : likeOutlineGrayIcon;
    likeButton.setIcon(icon);
    if (icon == null) {
      String heart = liked ? "\u2665" : "\u2661";
      likeButton.setText(heart + " " + post.likeCount());
    } else {
      likeButton.setText(String.valueOf(post.likeCount()));
    }
    likeButton.setForeground(liked ? FAU_RED : TEXT_SECONDARY);
    likeButton.setToolTipText(liked ? "Unlike" : "Like");
  }

  private void handleLike() {
    controller.toggleLike(post);
    SwingUtilities.invokeLater(this::updateLikeButton);
  }

  private void handleOpen() {
    var window = SwingUtilities.getWindowAncestor(this);
    if (window instanceof javax.swing.JFrame frame) {
      PostDetailDialog.showDialog(frame, post, commentController, session);
    }
  }

  private void handleDelete() {
    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to delete this post?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    if (confirm == JOptionPane.YES_OPTION) {
      controller.delete(post);
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

  private String extractInitial(String value) {
    if (value == null || value.isBlank()) {
      return "?";
    }
    return value.substring(0, 1).toUpperCase();
  }

  private String formatCategory(String category) {
    String[] words = category.replace("_", " ").split(" ");
    StringBuilder result = new StringBuilder();
    for (String word : words) {
      if (!word.isEmpty()) {
        result.append(word.substring(0, 1).toUpperCase())
            .append(word.substring(1).toLowerCase())
            .append(" ");
      }
    }
    return result.toString().trim();
  }

  private ImageIcon tintIcon(ImageIcon source, Color color) {
    if (source == null) {
      return null;
    }
    int width = source.getIconWidth();
    int height = source.getIconHeight();
    BufferedImage tinted = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = tinted.createGraphics();
    g2.drawImage(source.getImage(), 0, 0, null);
    g2.setComposite(AlphaComposite.SrcAtop);
    g2.setColor(color);
    g2.fillRect(0, 0, width, height);
    g2.dispose();
    return new ImageIcon(tinted);
  }

  private Font emojiCapable(Font base) {
    String[] candidates = { "Segoe UI Emoji", base.getFamily() };
    var available = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    for (String name : candidates) {
      for (String avail : available) {
        if (avail.equalsIgnoreCase(name)) {
          return new Font(avail, base.getStyle(), base.getSize());
        }
      }
    }
    return base;
  }
}
