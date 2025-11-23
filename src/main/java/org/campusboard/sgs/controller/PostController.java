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
  private FilterStrategy sortStrategy = new SortByNew();
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
    List<Post> base = posts.findAll();
    List<Post> afterSearch = applySearch(base);

    List<Post> filtered = (filterStrategy == null ? afterSearch.stream() : filterStrategy.filter(afterSearch))
        .collect(Collectors.toList());

    return (sortStrategy == null ? filtered.stream() : sortStrategy.filter(filtered))
        .collect(Collectors.toList());
  }

  public void setFilter(FilterStrategy strategy) {
    this.filterStrategy = strategy == null ? new AllFilter() : strategy;
    bus.publish(Events.FILTER_CHANGED, filterStrategy.getDescription());
  }

  public FilterStrategy getFilter() {
    return filterStrategy;
  }

  public void setSort(FilterStrategy strategy) {
    this.sortStrategy = strategy == null ? new SortByNew() : strategy;
    bus.publish(Events.FILTER_CHANGED, sortStrategy.getDescription());
  }

  public FilterStrategy getSort() {
    return sortStrategy;
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

  public boolean canModifyPost(Post post) {
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

  public java.util.Optional<Post> findById(java.util.UUID id) {
    return posts.findById(id);
  }

  private List<Post> applySearch(List<Post> source) {
    if (search == null || search.isBlank()) {
      return source;
    }
    String searchLower = search.toLowerCase();
    return source.stream()
        .filter(p -> p.title().toLowerCase().contains(searchLower) ||
            p.body().toLowerCase().contains(searchLower) ||
            p.author().toLowerCase().contains(searchLower))
        .collect(Collectors.toList());
  }
}
