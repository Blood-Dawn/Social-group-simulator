package org.campusboard.sgs.view.dialogs;

import org.campusboard.sgs.controller.PostController;
import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.util.EventBus;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class ModeratePostsDialog extends JDialog {
  private final PostController postController;
  private final EventBus bus;
  private JTable postTable;
  private DefaultTableModel tableModel;

  public ModeratePostsDialog(Frame owner, PostController postController, EventBus bus) {
    super(owner, "Moderate Posts (test)", true);
    this.postController = postController;
    this.bus = bus;

    setLayout(new BorderLayout(10, 10));
    setSize(800, 500);
    setLocationRelativeTo(owner);

    add(createTablePanel(), BorderLayout.CENTER);
    add(createButtonPanel(), BorderLayout.SOUTH);

    loadPosts();
  }

  private JPanel createTablePanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    String[] columnNames = {"Title", "Author", "Category", "Likes", "Actions"};
    tableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return column == 4; // Only Actions column editable
      }
    };

    postTable = new JTable(tableModel);
    postTable.setRowHeight(35);
    postTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
    postTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

    JScrollPane scrollPane = new JScrollPane(postTable);
    panel.add(scrollPane, BorderLayout.CENTER);

    return panel;
  }

  private JPanel createButtonPanel() {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

    JButton refreshButton = new JButton("Refresh");
    refreshButton.addActionListener(e -> loadPosts());

    JButton closeButton = new JButton("Close");
    closeButton.addActionListener(e -> dispose());

    panel.add(refreshButton);
    panel.add(closeButton);

    return panel;
  }

  private void loadPosts() {
    tableModel.setRowCount(0);
    List<Post> posts = postController.current();
    for (Post post : posts) {
      tableModel.addRow(new Object[]{
          post.title().length() > 30 ? post.title().substring(0, 30) + "..." : post.title(),
          post.author(),
          post.category().name(),
          post.likeCount(),
          post // Store the post object for deletion
      });
    }
  }

  class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
      setOpaque(true);
      setText("Delete");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
      return this;
    }
  }

  class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private boolean clicked;
    private int currentRow;

    public ButtonEditor(JCheckBox checkBox) {
      super(checkBox);
      button = new JButton("Delete");
      button.setOpaque(true);
      button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
      clicked = true;
      currentRow = row;
      return button;
    }

    @Override
    public Object getCellEditorValue() {
      if (clicked) {
        Post post = (Post) tableModel.getValueAt(currentRow, 4);
        handleDelete(post);
      }
      clicked = false;
      return "Delete";
    }

    @Override
    public boolean stopCellEditing() {
      clicked = false;
      return super.stopCellEditing();
    }
  }

  private void handleDelete(Post post) {
    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to delete the post:\n\"" + post.title() + "\"?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION);

    if (confirm == JOptionPane.YES_OPTION) {
      postController.delete(post);
      JOptionPane.showMessageDialog(this,
          "Post deleted successfully",
          "Success",
          JOptionPane.INFORMATION_MESSAGE);
      loadPosts(); // Refresh the table
    }
  }

  public static void showDialog(Frame owner, PostController postController, EventBus bus) {
    ModeratePostsDialog dialog = new ModeratePostsDialog(owner, postController, bus);
    dialog.setVisible(true);
  }
}
