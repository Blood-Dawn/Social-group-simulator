package org.campusboard.sgs.controller;

import org.campusboard.sgs.model.*;
import org.campusboard.sgs.repo.*;
import org.campusboard.sgs.util.*;
import java.util.*;

public class PostController {
  private final PostRepository posts;
  private final Session session;
  private final EventBus bus;
  private Category filter = null;
  private String search = null;

  public PostController(PostRepository posts, Session session, EventBus bus){
    this.posts=posts; this.session=session; this.bus=bus;
  }

  public List<Post> current(){ return posts.find(filter, search); }
  public void setFilter(Category c){ this.filter = c; bus.publish(Events.FILTER_CHANGED); }
  public void setSearch(String s){ this.search = (s==null||s.isBlank())?null:s; bus.publish(Events.SEARCH_CHANGED, this.search); }

  public void create(String title, String body, Category cat){
    var author = session.isAuthenticated()? session.user().username() : "guest";
    posts.save(new Post(null,title,body,cat,author));
    bus.publish(Events.POSTS_REPLACED);
  }

  /** Like toggle; guests are forced to login prompt. */
  public void toggleLike(Post p){
    if (!session.isAuthenticated()){ bus.publish(Events.SHOW_LOGIN); return; }
    p.toggleLike(session.user().username());
    posts.update(p);
    bus.publish(Events.POST_UPDATED, p.id()); // partial update, not whole feed
  }

  public void delete(UUID id){
    if (session.role()==Role.ADMIN || session.role()==Role.STAFF) {
      posts.delete(id);
      bus.publish(Events.POSTS_REPLACED);
    }
  }
}
