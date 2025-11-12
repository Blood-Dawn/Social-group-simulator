package org.campusboard.sgs.controller;

import org.campusboard.sgs.model.*;
import org.campusboard.sgs.repo.*;
import org.campusboard.sgs.util.*;
import org.campusboard.sgs.filter.*;
import java.util.*;
import java.util.stream.Collectors;

public class PostController {
  private final PostRepository posts;
  private final UserRepository users;
  private final Session session;
  private final EventBus bus;
  private final UndoManager undoManager;
  private FilterStrategy filterStrategy = new AllFilter();
  private String search = null;

  public PostController(PostRepository posts, UserRepository users, Session session, EventBus bus) {
    this.posts = posts;
    this.users = users;
    this.session = session;
    this.bus = bus;
    this.undoManager = new UndoManager();
  }

  public List<Post> current() {
    List<Post> filtered = filterStrategy.filter(posts.findAll())
        .collect(Collectors.toList());

    if (search == null || search.isBlank()) {
      return filtered;
    }

    String searchLower = search.toLowerCase();
    return filtered.stream()
        .filter(p -> p.title().toLowerCase().contains(searchLower) ||
            p.body().toLowerCase().contains(searchLower) ||
            p.author().toLowerCase().contains(searchLower))
        .collect(Collectors.toList());
  }

  public void setFilter(FilterStrategy strategy) {
    this.filterStrategy = strategy;
    bus.publish(Events.FILTER_CHANGED, null);
  }

  public FilterStrategy getFilter() {
    return filterStrategy;
  }

  public void setSearch(String s) {
    this.search = (s == null || s.isBlank()) ? null : s;
    bus.publish(Events.SEARCH_CHANGED, this.search);
  }

  public void create(String title, String body, Category cat) {
    var author = session.isAuthenticated() ? session.user().username() : "guest";
    var post = new Post(null, title, body, cat, author);
    undoManager.execute(new CreatePostCommand(posts, bus, post));
  }

  public void edit(Post post, String title, String body, Category cat) {
    if (!canModifyPost(post))
      return;
    undoManager.execute(new EditPostCommand(posts, bus, post, title, body, cat));
  }

  public void delete(Post post) {
    if (!canModifyPost(post))
      return;
    undoManager.execute(new DeletePostCommand(posts, bus, post));
  }

  public void toggleLike(Post p) {
    if (!session.isAuthenticated()) {
      bus.publish(Events.SHOW_LOGIN, null);
      return;
    }
    undoManager.execute(new LikePostCommand(posts, bus, p, session.user().username()));
  }

  public void undo() {
    undoManager.undo();
  }

  public void redo() {
    undoManager.redo();
  }

  public boolean canUndo() {
    return undoManager.canUndo();
  }

  public boolean canRedo() {
    return undoManager.canRedo();
  }

  public String getUndoDescription() {
    return undoManager.getUndoDescription();
  }

  public String getRedoDescription() {
    return undoManager.getRedoDescription();
  }

  public boolean isValidAuthor(String username) {
    return users.find(username).isPresent();
  }

  private boolean canModifyPost(Post post) {
    if (!session.isAuthenticated())
      return false;
    Role role = session.role();
    // Only ADMIN can modify any post, others can only modify their own
    if (role == Role.ADMIN)
      return true;
    return post.author().equals(session.user().username());
  }

  public UndoManager getUndoManager() {
    return undoManager;
  }
}
