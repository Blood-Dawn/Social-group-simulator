package org.campusboard.sgs.view;

import org.campusboard.sgs.controller.*;
import org.campusboard.sgs.model.*;
import javax.swing.*;
import java.awt.*;

public class PostCard extends JPanel {
  private final PostController controller;
  private final JButton likeBtn = new JButton();
  private Post post;

  public PostCard(PostController c){
    super(new BorderLayout());
    this.controller = c;
    setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(Color.LIGHT_GRAY),
      BorderFactory.createEmptyBorder(8,8,8,8)
    ));
    var north = new JPanel(new BorderLayout());
    var title = new JLabel(); title.setName("title");
    north.add(title, BorderLayout.WEST);

    likeBtn.addActionListener(e -> controller.toggleLike(post));
    var south = new JPanel(new FlowLayout(FlowLayout.LEFT));
    south.add(likeBtn);

    add(north, BorderLayout.NORTH);
    add(new JLabel(), BorderLayout.CENTER); // body placeholder; keep brief
    add(south, BorderLayout.SOUTH);
  }

  public void bind(Post p){
    this.post = p;
    ((JLabel)((JPanel)getComponent(0)).getComponent(0)).setText(p.title());
    ((JLabel)getComponent(1)).setText("<html>"+p.body()+"</html>");
    likeBtn.setText((p.likeCount()>0?"Unlike":"Like")+" ("+p.likeCount()+")");
  }
}
