package org.campusboard.sgs.view;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import org.campusboard.sgs.controller.Controller;
import org.campusboard.sgs.model.Post;

/**
 * Individual post card component with modern design
 * Features: Circular avatar, formatted timestamps, hover effects
 */
public class PostCard extends JPanel {
    private Controller controller;
    private Post post;
    
    private static final Color FAU_NAVY = new Color(0, 51, 102);
    private static final Color FAU_RED = new Color(206, 17, 65);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color HOVER_COLOR = new Color(248, 249, 250);
    private static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    
    public PostCard(Post post, Controller controller) {
        this.post = post;
        this.controller = controller;
        
        initializeModernCard();
    }
    
    /**
     * Initialize card with shadow border and hover effect
     */
    private void initializeModernCard() {
        setLayout(new BorderLayout(15, 15));
        setBackground(CARD_BACKGROUND);
        
        // Shadow border effect
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 2, 6, 2),
                BorderFactory.createLineBorder(new Color(0, 0, 0, 20), 1)
            ),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Hover effect - light gray on mouse over
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(HOVER_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(CARD_BACKGROUND);
            }
        });
        
        add(createHeaderSection(), BorderLayout.NORTH);
        add(createContentSection(), BorderLayout.CENTER);
        add(createFooterSection(), BorderLayout.SOUTH);
    }
    
    /**
     * Create header with avatar, username, timestamp, category badge, delete button
     */
    private JPanel createHeaderSection() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        
        // LEFT - Avatar + User info
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        userInfo.setOpaque(false);
        
        String author = post.getAuthor();
        String initial = author != null ? author.substring(0, 1).toUpperCase() : "?";
        JLabel avatar = createCircularAvatar(initial);
        userInfo.add(avatar);
        
        JPanel textInfo = new JPanel();
        textInfo.setLayout(new BoxLayout(textInfo, BoxLayout.Y_AXIS));
        textInfo.setOpaque(false);
        
        JLabel username = new JLabel("@" + (author != null ? author : "unknown"));
        username.setFont(new Font("Arial", Font.BOLD, 13));
        username.setForeground(TEXT_PRIMARY);
        
        JLabel timestamp = new JLabel(formatTimestamp(post.getCreatedAt()));
        timestamp.setFont(new Font("Arial", Font.PLAIN, 11));
        timestamp.setForeground(TEXT_SECONDARY);
        
        textInfo.add(username);
        textInfo.add(timestamp);
        userInfo.add(textInfo);
        
        // RIGHT - Category badge + Delete button
        JPanel rightSide = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightSide.setOpaque(false);
        
        JLabel categoryBadge = createRoundedBadge(post.getCategory().name());
        rightSide.add(categoryBadge);
        
        JButton deleteBtn = createIconButton("×");
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 20));
        deleteBtn.setToolTipText("Delete post");
        deleteBtn.addActionListener(e -> handleDelete());
        rightSide.add(deleteBtn);
        
        header.add(userInfo, BorderLayout.WEST);
        header.add(rightSide, BorderLayout.EAST);
        
        return header;
    }
    
    /**
     * Create content section with title (centered) and body (left-aligned)
     */
    private JPanel createContentSection() {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        
        // Title - centered
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel title = new JLabel(post.getTitle());
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setForeground(FAU_NAVY);
        
        titlePanel.add(Box.createHorizontalGlue());
        titlePanel.add(title);
        titlePanel.add(Box.createHorizontalGlue());
        
        // Body - left aligned with word wrap
        JTextArea body = new JTextArea(post.getBody());
        body.setFont(new Font("Arial", Font.PLAIN, 14));
        body.setForeground(TEXT_PRIMARY);
        body.setLineWrap(true);
        body.setWrapStyleWord(true);
        body.setEditable(false);
        body.setOpaque(false);
        body.setBorder(null);
        body.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        content.add(titlePanel);
        content.add(Box.createVerticalStrut(12));
        content.add(body);
        
        return content;
    }
    
    /**
     * Create footer with like button
     */
    private JPanel createFooterSection() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        footer.setOpaque(false);
        
        JButton likeBtn = createActionButton("♥", post.getLikes());
        likeBtn.addActionListener(e -> handleLike());
        footer.add(likeBtn);
        
        return footer;
    }
    
    /**
     * Create circular avatar with painted graphics
     * Uses custom paintComponent for smooth circle
     */
    private JLabel createCircularAvatar(String initial) {
        JLabel avatar = new JLabel(initial) {
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
        avatar.setPreferredSize(new Dimension(40, 40));
        avatar.setFont(new Font("Arial", Font.BOLD, 16));
        avatar.setForeground(Color.WHITE);
        avatar.setBackground(FAU_RED);
        avatar.setOpaque(false);
        return avatar;
    }
    
    /**
     * Create rounded category badge
     */
    private JLabel createRoundedBadge(String category) {
        String formatted = formatCategoryName(category);
        JLabel badge = new JLabel(formatted);
        badge.setFont(new Font("Arial", Font.BOLD, 11));
        badge.setForeground(FAU_NAVY);
        badge.setBackground(new Color(225, 239, 254));
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 230, 255), 1),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        return badge;
    }
    
    /**
     * Create action button (like) with hover effect
     */
    private JButton createActionButton(String emoji, int count) {
        JButton btn = new JButton(emoji + " " + count);
        btn.setFont(new Font("Arial", Font.PLAIN, 13));
        btn.setForeground(TEXT_SECONDARY);
        btn.setBackground(CARD_BACKGROUND);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setForeground(FAU_NAVY);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setForeground(TEXT_SECONDARY);
            }
        });
        
        return btn;
    }
    
    /**
     * Create icon button (delete) with hover effect
     */
    private JButton createIconButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.PLAIN, 16));
        btn.setForeground(TEXT_SECONDARY);
        btn.setBackground(CARD_BACKGROUND);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setForeground(FAU_RED);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setForeground(TEXT_SECONDARY);
            }
        });
        
        return btn;
    }
    
    /**
     * Handle like button click
     */
    private void handleLike() {
        System.out.println("♥ PostCard: Like button clicked for post: " + post.getId());
        controller.likePost(post.getId());
    }
    
    /**
     * Handle delete button click with confirmation
     */
    private void handleDelete() {
        System.out.println("× PostCard: Delete button clicked");
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this post?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            controller.deletePost(post.getId());
        }
    }
    
    /**
     * Format timestamp to human-readable string
     * Examples: "Just now", "5 minutes ago", "3 hours ago", "Oct 14, 2025"
     */
    private String formatTimestamp(LocalDateTime createdAt) {
        if (createdAt == null) return "Unknown";
        
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(createdAt, now);
        
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();
        
        if (minutes < 1) return "Just now";
        if (minutes < 60) return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        if (hours < 24) return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        if (days < 7) return days + " day" + (days > 1 ? "s" : "") + " ago";
        
        return createdAt.format(DateTimeFormatter.ofPattern("MMM d, yyyy"));
    }
    
    /**
     * Format category from ENUM_CASE to Title Case
     */
    private String formatCategoryName(String category) {
        String[] words = category.replace("_", " ").split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                result.append(word.substring(0, 1).toUpperCase())
                      .append(word.substring(1).toLowerCase())
                      .append(" ");
            }
        }
        return result.toString().trim();
    }
}