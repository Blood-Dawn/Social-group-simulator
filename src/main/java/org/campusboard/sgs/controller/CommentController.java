package org.campusboard.sgs.controller;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.campusboard.sgs.model.Comment;
import org.campusboard.sgs.repo.CommentRepository;
import org.campusboard.sgs.util.EventBus;
import org.campusboard.sgs.util.Events;
import org.campusboard.sgs.util.Session;

public class CommentController {
  private final CommentRepository comments;
  private final Session session;
  private final EventBus bus;

  public CommentController(CommentRepository comments, Session session, EventBus bus) {
    this.comments = comments;
    this.session = session;
    this.bus = bus;
  }

  public List<Comment> listForPost(UUID postId) {
    return comments.findByPost(postId).stream()
        .sorted(Comparator.comparing(Comment::createdAt).reversed())
        .toList();
  }

  public boolean addComment(UUID postId, String body) {
    if (!session.isAuthenticated()) {
      bus.publish(Events.SHOW_LOGIN, null);
      return false;
    }
    var author = session.user().username();
    comments.add(new Comment(null, postId, author, body, LocalDateTime.now()));
    bus.publish(Events.POST_UPDATED, postId); // reuse existing event to refresh detail if needed
    return true;
  }
}
