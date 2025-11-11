package org.campusboard.sgs;

import org.campusboard.sgs.repo.*;
import org.campusboard.sgs.util.*;
import org.campusboard.sgs.controller.*;
import org.campusboard.sgs.view.*;
import javax.swing.*;
import java.awt.*;

public class Main {
  public static void main(String[] args){
    SwingUtilities.invokeLater(Main::start);
  }
  private static void start(){
    var bus = new EventBus();
    var session = new Session();
    var users = new InMemoryUserRepository();
    var posts = new InMemoryPostRepository();

    var authCtl = new AuthController(users, session, bus);
    var postCtl = new PostController(posts, session, bus);

    var frame = new JFrame("CampusBoard");
    var top = new TopBar(postCtl, authCtl, bus);
    var feed = new FeedPanel(postCtl, bus);
    var side = new SideBar(postCtl, bus);

    var split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, side, feed);
    split.setOneTouchExpandable(true);
    split.setDividerLocation(260);

    frame.setLayout(new BorderLayout());
    frame.add(top, BorderLayout.NORTH);
    frame.add(split, BorderLayout.CENTER);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1000, 700);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
