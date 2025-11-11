package org.campusboard.sgs.view.dialogs;

import org.campusboard.sgs.controller.PostController;
import org.campusboard.sgs.model.Post;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ViewReportsDialog extends JDialog {
  private final PostController postController;

  public ViewReportsDialog(Frame owner, PostController postController) {
    super(owner, "View Reports", true);
    this.postController = postController;

    setLayout(new BorderLayout(10, 10));
    setSize(600, 500);
    setLocationRelativeTo(owner);

    add(createReportPanel(), BorderLayout.CENTER);
    add(createButtonPanel(), BorderLayout.SOUTH);
  }

  private JPanel createReportPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    JLabel title = new JLabel("System Reports");
    title.setFont(new Font("Arial", Font.BOLD, 18));
    title.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(title);
    panel.add(Box.createVerticalStrut(15));

    // Generate reports
    List<Post> allPosts = postController.current();

    // Total posts
    panel.add(createReportItem("Total Posts:", String.valueOf(allPosts.size())));
    panel.add(Box.createVerticalStrut(10));

    // Total likes
    int totalLikes = allPosts.stream()
        .mapToInt(Post::likeCount)
        .sum();
    panel.add(createReportItem("Total Likes:", String.valueOf(totalLikes)));
    panel.add(Box.createVerticalStrut(10));

    // Posts by category
    Map<String, Long> postsByCategory = allPosts.stream()
        .collect(Collectors.groupingBy(p -> p.category().name(), Collectors.counting()));

    JLabel categoryLabel = new JLabel("Posts by Category:");
    categoryLabel.setFont(new Font("Arial", Font.BOLD, 14));
    categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(categoryLabel);
    panel.add(Box.createVerticalStrut(5));

    for (Map.Entry<String, Long> entry : postsByCategory.entrySet()) {
      panel.add(createReportItem("  " + entry.getKey() + ":", String.valueOf(entry.getValue())));
      panel.add(Box.createVerticalStrut(5));
    }
    panel.add(Box.createVerticalStrut(10));

    // Most liked post
    Post mostLiked = allPosts.stream()
        .max((p1, p2) -> Integer.compare(p1.likeCount(), p2.likeCount()))
        .orElse(null);

    if (mostLiked != null) {
      panel.add(createReportItem("Most Liked Post:",
          mostLiked.title() + " (" + mostLiked.likeCount() + " likes)"));
    }

    panel.add(Box.createVerticalGlue());

    return panel;
  }

  private JPanel createReportItem(String label, String value) {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

    JLabel labelComp = new JLabel(label);
    labelComp.setFont(new Font("Arial", Font.PLAIN, 13));

    JLabel valueComp = new JLabel(value);
    valueComp.setFont(new Font("Arial", Font.BOLD, 13));

    panel.add(labelComp);
    panel.add(valueComp);

    return panel;
  }

  private JPanel createButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

    JButton closeButton = new JButton("Close");
    closeButton.addActionListener(e -> dispose());

    panel.add(closeButton);

    return panel;
  }

  public static void showDialog(Frame owner, PostController postController) {
    ViewReportsDialog dialog = new ViewReportsDialog(owner, postController);
    dialog.setVisible(true);
  }
}
