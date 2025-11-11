package org.campusboard.sgs;

import javax.swing.*;
import java.awt.*;
import org.campusboard.sgs.controller.*;
import org.campusboard.sgs.model.*;
import org.campusboard.sgs.repo.*;
import org.campusboard.sgs.util.*;
import org.campusboard.sgs.view.*;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(Main::start);
  }

  private static void start() {
    var bus = new EventBus();
    var session = new Session();
    var users = new InMemoryUserRepository();
    var posts = new InMemoryPostRepository();

    seedUsers(users);

    var authCtl = new AuthController(users, session, bus);
    var postCtl = new PostController(posts, session, bus);

    var frame = new JFrame("CampusBoard");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1200, 800);
    frame.setLocationRelativeTo(null);

    var top = new TopBar(postCtl, authCtl, bus);
    var feed = new FeedPanel(postCtl, bus);
    var side = new SideBar(postCtl, bus);

    var split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, side, feed);
    split.setOneTouchExpandable(true);
    split.setDividerLocation(260);
    split.setResizeWeight(0.2);

    frame.add(top, BorderLayout.NORTH);
    frame.add(split, BorderLayout.CENTER);
    frame.setVisible(true);
  }

  private static void seedUsers(InMemoryUserRepository users) {
    users.add(new User("admin", "admin123", Role.ADMIN));
    users.add(new User("staff01", "staff123", Role.STAFF));
    users.add(new User("stud01", "student123", Role.STUDENT));
    users.add(new User("stud02", "student123", Role.STUDENT));
  }
}
