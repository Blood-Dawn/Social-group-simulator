package org.campusboard.sgs.view;

import org.campusboard.sgs.controller.*;
import org.campusboard.sgs.model.*;
import org.campusboard.sgs.util.*;
import javax.swing.*;
import java.awt.*;

public class SideBar extends JPanel {
  public SideBar(PostController controller, EventBus bus){
    setLayout(new GridLayout(0,1,4,4));
    add(button("All", ()-> controller.setFilter(null)));
    add(button("Announcements", ()-> controller.setFilter(Category.ANNOUNCEMENTS)));
    add(button("Study Groups", ()-> controller.setFilter(Category.STUDY_GROUPS)));
    add(button("Events", ()-> controller.setFilter(Category.EVENTS)));
    add(button("Lost & Found", ()-> controller.setFilter(Category.LOST_FOUND)));
    var guest = new JLabel("<html><b>Guest mode:</b><br>Login to access</html>");
    add(guest);
  }
  private JButton button(String text, Runnable r){
    var b = new JButton(text); b.addActionListener(e->r.run()); return b;
  }
}
