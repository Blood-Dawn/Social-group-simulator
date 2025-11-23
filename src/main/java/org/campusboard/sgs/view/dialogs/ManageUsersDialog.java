package org.campusboard.sgs.view.dialogs;

import org.campusboard.sgs.model.*;
import org.campusboard.sgs.repo.UserRepository;
import org.campusboard.sgs.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class ManageUsersDialog extends JDialog {
  private final UserRepository userRepo;
  private final EventBus bus;
  private JTable userTable;
  private DefaultTableModel tableModel;

  public ManageUsersDialog(Frame owner, UserRepository userRepo, EventBus bus) {
    super(owner, "Manage Users", true);
    this.userRepo = userRepo;
    this.bus = bus;

    setLayout(new BorderLayout(10, 10));
    setSize(600, 400);
    setLocationRelativeTo(owner);

    add(createTablePanel(), BorderLayout.CENTER);
    add(createButtonPanel(), BorderLayout.SOUTH);

    loadUsers();
  }

  private JPanel createTablePanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    String[] columnNames = { "Username", "Role", "Actions" };
    tableModel = new DefaultTableModel(columnNames, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return column == 2; // Only Actions column editable
      }
    };

    userTable = new JTable(tableModel);
    userTable.setRowHeight(30);
    userTable.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
    userTable.getColumnModel().getColumn(2).setCellEditor(new ButtonEditor(new JCheckBox()));

    JScrollPane scrollPane = new JScrollPane(userTable);
    panel.add(scrollPane, BorderLayout.CENTER);

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

  private void loadUsers() {
    tableModel.setRowCount(0);
    userRepo.listAll().stream()
        .sorted(java.util.Comparator.comparing(User::username))
        .forEach(u -> tableModel.addRow(new Object[] { u.username(), u.role().name(), "Toggle Role" }));
  }

  class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
      setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus,
        int row, int column) {
      setText((value == null) ? "Toggle Role" : value.toString());
      return this;
    }
  }

  class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean clicked;
    private int currentRow;

    public ButtonEditor(JCheckBox checkBox) {
      super(checkBox);
      button = new JButton();
      button.setOpaque(true);
      button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
        boolean isSelected, int row, int column) {
      label = (value == null) ? "Toggle Role" : value.toString();
      button.setText(label);
      clicked = true;
      currentRow = row;
      return button;
    }

    @Override
    public Object getCellEditorValue() {
      if (clicked) {
        String username = (String) tableModel.getValueAt(currentRow, 0);
        String currentRole = (String) tableModel.getValueAt(currentRow, 1);
        handleToggleRole(username, currentRole);
      }
      clicked = false;
      return label;
    }

    @Override
    public boolean stopCellEditing() {
      clicked = false;
      return super.stopCellEditing();
    }
  }

  private void handleToggleRole(String username, String currentRoleStr) {
    Role currentRole = Role.valueOf(currentRoleStr);
    Role newRole;

    // Cycle through roles: GUEST -> STUDENT -> STAFF -> ADMIN -> GUEST
    newRole = switch (currentRole) {
      case GUEST -> Role.STUDENT;
      case STUDENT -> Role.STAFF;
      case STAFF -> Role.ADMIN;
      case ADMIN -> Role.GUEST;
    };

    // Update the user in repository (in memory)
    userRepo.find(username).ifPresent(user -> {
      // Remove old user and add new one with updated role
      User updatedUser = new User(username, user.password(), newRole);
      userRepo.add(updatedUser);

      // Update the table display
      for (int i = 0; i < tableModel.getRowCount(); i++) {
        if (tableModel.getValueAt(i, 0).equals(username)) {
          tableModel.setValueAt(newRole.name(), i, 1);
          break;
        }
      }

      // Publish event to notify other components
      bus.publish(Events.USER_LOGGED_OUT, username);

      JOptionPane.showMessageDialog(this,
          "Role updated to " + newRole.name() + " for user " + username,
          "Success",
          JOptionPane.INFORMATION_MESSAGE);
    });
  }

  public static void showDialog(Frame owner, UserRepository userRepo, EventBus bus) {
    ManageUsersDialog dialog = new ManageUsersDialog(owner, userRepo, bus);
    dialog.setVisible(true);
  }
}
