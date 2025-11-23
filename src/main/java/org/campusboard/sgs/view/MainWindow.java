package org.campusboard.sgs.view;

import org.campusboard.sgs.controller.*;
import org.campusboard.sgs.model.Role;
import org.campusboard.sgs.repo.*;
import org.campusboard.sgs.util.*;
import org.campusboard.sgs.view.dialogs.*;
import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
  private final PostController postController;
  private final AuthController authController;
  private final UserRepository userRepo;
  private final Session session;
  private final EventBus bus;

  private JMenu adminMenu;
  private JMenuItem undoItem;
  private JMenuItem redoItem;

  public MainWindow(PostController postController,
      AuthController authController,
      UserRepository userRepo,
      Session session,
      EventBus bus) {
    super("CampusBoard");
    this.postController = postController;
    this.authController = authController;
    this.userRepo = userRepo;
    this.session = session;
    this.bus = bus;

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1200, 800);
    setLocationRelativeTo(null);

    createMenuBar();
    createContent();

    // Subscribe to login/logout events to update menu
    bus.subscribe(Events.USER_LOGGED_IN, e -> updateMenus());
    bus.subscribe(Events.USER_LOGGED_OUT, e -> updateMenus());

    updateMenus();
  }

  private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    // File menu
    JMenu fileMenu = new JMenu("File");
    JMenuItem exitItem = new JMenuItem("Exit");
    exitItem.addActionListener(e -> System.exit(0));
    fileMenu.add(exitItem);
    menuBar.add(fileMenu);

    // Edit menu
    JMenu editMenu = new JMenu("Edit");
    undoItem = new JMenuItem("Undo");
    undoItem.setAccelerator(KeyStroke.getKeyStroke("control Z"));
    undoItem.addActionListener(e -> postController.undo());

    redoItem = new JMenuItem("Redo");
    redoItem.setAccelerator(KeyStroke.getKeyStroke("control Y"));
    redoItem.addActionListener(e -> postController.redo());

    editMenu.add(undoItem);
    editMenu.add(redoItem);
    menuBar.add(editMenu);

    // Post menu
    JMenu postMenu = new JMenu("Post");
    JMenuItem createPostItem = new JMenuItem("Create Post");
    createPostItem.setAccelerator(KeyStroke.getKeyStroke("control N"));
    createPostItem.addActionListener(e -> showCreatePostDialog());
    postMenu.add(createPostItem);
    menuBar.add(postMenu);

    // Admin menu (initially hidden)
    adminMenu = new JMenu("Admin");
    JMenuItem manageUsersItem = new JMenuItem("Manage Users");
    manageUsersItem.addActionListener(e -> ManageUsersDialog.showDialog(this, userRepo, bus));

    JMenuItem moderatePostsItem = new JMenuItem("Moderate Posts");
    moderatePostsItem.addActionListener(e -> ModeratePostsDialog.showDialog(this, postController, bus));

    JMenuItem viewReportsItem = new JMenuItem("View Reports");
    viewReportsItem.addActionListener(e -> ViewReportsDialog.showDialog(this, postController));

    adminMenu.add(manageUsersItem);
    adminMenu.add(moderatePostsItem);
    adminMenu.add(viewReportsItem);
    menuBar.add(adminMenu);

    // Help menu
    JMenu helpMenu = new JMenu("Help");
    JMenuItem aboutItem = new JMenuItem("About");
    aboutItem.addActionListener(e -> showAboutDialog());
    helpMenu.add(aboutItem);
    menuBar.add(helpMenu);

    setJMenuBar(menuBar);
  }

  private void createContent() {
    setLayout(new BorderLayout());

    var top = new TopBar(postController, authController, bus, session);
    var feed = new FeedPanel(postController, session, bus);
    var sidebar = new SidebarPanel(postController, bus, session, userRepo);

    var split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, feed);
    split.setOneTouchExpandable(true);
    split.setDividerLocation(260);
    split.setResizeWeight(0.2);

    add(top, BorderLayout.NORTH);
    add(split, BorderLayout.CENTER);
  }

  private void updateMenus() {
    SwingUtilities.invokeLater(() -> {
      // Update undo/redo items
      undoItem.setEnabled(postController.canUndo());
      undoItem.setText("Undo" +
          (postController.canUndo() ? " " + postController.getUndoDescription() : ""));

      redoItem.setEnabled(postController.canRedo());
      redoItem.setText("Redo" +
          (postController.canRedo() ? " " + postController.getRedoDescription() : ""));

      // Show/hide admin menu based on role
      boolean isAdmin = session.isAuthenticated() && session.role() == Role.ADMIN;
      adminMenu.setVisible(isAdmin);
    });
  }

  private void showCreatePostDialog() {
    CreatePostDialog.showDialog(this, postController);
  }

  private void showAboutDialog() {
    JOptionPane.showMessageDialog(this,
        "CampusBoard\nVersion 1.0\n\n" +
            "A social posting system for campus communities",
        "About CampusBoard",
        JOptionPane.INFORMATION_MESSAGE);
  }
}
