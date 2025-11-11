package org.campusboard.sgs.view;

import org.campusboard.sgs.controller.*;
import org.campusboard.sgs.util.*;
import javax.swing.*;
import java.awt.*;

public class TopBar extends JPanel {
  public TopBar(PostController posts, AuthController auth, EventBus bus){
    setLayout(new BorderLayout(8,0));
    var search = new JTextField(); search.setName("searchField");
    search.addActionListener(e -> posts.setSearch(search.getText()));
    add(search, BorderLayout.CENTER);

    var login = new JButton("Login");
    login.addActionListener(e -> {
      String u = JOptionPane.showInputDialog(this,"Username:");
      String p = JOptionPane.showInputDialog(this,"Password:");
      if (u!=null && p!=null && auth.login(u,p)) {
        JOptionPane.showMessageDialog(this,"Logged in as "+u);
      } else {
        JOptionPane.showMessageDialog(this,"Login failed");
      }
    });
    add(login, BorderLayout.EAST);
  }
}
