package org.campusboard.sgs.view.dialogs;

import org.campusboard.sgs.controller.PostController;
import org.campusboard.sgs.model.Category;
import javax.swing.*;
import java.awt.*;

public class CreatePostDialog extends JDialog {
  private final PostController controller;
  private JTextField titleField;
  private JTextArea bodyArea;
  private JComboBox<Category> categoryCombo;
  private boolean success = false;

  public CreatePostDialog(Frame owner, PostController controller) {
    super(owner, "Create Post", true);
    this.controller = controller;

    setLayout(new BorderLayout(10, 10));
    setSize(500, 400);
    setLocationRelativeTo(owner);

    add(createFormPanel(), BorderLayout.CENTER);
    add(createButtonPanel(), BorderLayout.SOUTH);
  }

  private JPanel createFormPanel() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    // Title field
    JLabel titleLabel = new JLabel("Title:");
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(titleLabel);
    panel.add(Box.createVerticalStrut(5));

    titleField = new JTextField();
    titleField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
    titleField.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(titleField);
    panel.add(Box.createVerticalStrut(10));

    // Category combo
    JLabel categoryLabel = new JLabel("Category:");
    categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(categoryLabel);
    panel.add(Box.createVerticalStrut(5));

    categoryCombo = new JComboBox<>(Category.values());
    categoryCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
    categoryCombo.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(categoryCombo);
    panel.add(Box.createVerticalStrut(10));

    // Body area
    JLabel bodyLabel = new JLabel("Body:");
    bodyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(bodyLabel);
    panel.add(Box.createVerticalStrut(5));

    bodyArea = new JTextArea(10, 40);
    bodyArea.setLineWrap(true);
    bodyArea.setWrapStyleWord(true);
    JScrollPane scrollPane = new JScrollPane(bodyArea);
    scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(scrollPane);

    return panel;
  }

  private JPanel createButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(e -> {
      success = false;
      dispose();
    });

    JButton createButton = new JButton("Create");
    createButton.addActionListener(e -> handleCreate());

    panel.add(cancelButton);
    panel.add(createButton);

    return panel;
  }

  private void handleCreate() {
    String title = titleField.getText().trim();
    String body = bodyArea.getText().trim();
    Category category = (Category) categoryCombo.getSelectedItem();

    // Validation
    if (title.isEmpty()) {
      JOptionPane.showMessageDialog(this,
          "Title cannot be empty",
          "Validation Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    if (title.length() > 100) {
      JOptionPane.showMessageDialog(this,
          "Title must be 100 characters or less",
          "Validation Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    if (body.isEmpty()) {
      JOptionPane.showMessageDialog(this,
          "Body cannot be empty",
          "Validation Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    if (body.length() > 1000) {
      JOptionPane.showMessageDialog(this,
          "Body must be 1000 characters or less",
          "Validation Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    // Create the post
    controller.create(title, body, category);
    success = true;
    dispose();
  }

  public boolean isSuccess() {
    return success;
  }

  public static boolean showDialog(Frame owner, PostController controller) {
    CreatePostDialog dialog = new CreatePostDialog(owner, controller);
    dialog.setVisible(true);
    return dialog.isSuccess();
  }
}
