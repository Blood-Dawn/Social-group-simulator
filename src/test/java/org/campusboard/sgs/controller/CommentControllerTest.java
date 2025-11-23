package org.campusboard.sgs.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import org.campusboard.sgs.model.Category;
import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.model.Role;
import org.campusboard.sgs.model.User;
import org.campusboard.sgs.repo.InMemoryCommentRepository;
import org.campusboard.sgs.util.EventBus;
import org.campusboard.sgs.util.Session;
import org.junit.jupiter.api.Test;

class CommentControllerTest {

  @Test
  void addComment_requiresAuth() {
    var comments = new InMemoryCommentRepository();
    var session = new Session();
    var bus = new EventBus();
    var ctl = new CommentController(comments, session, bus);
    var postId = UUID.randomUUID();

    boolean ok = ctl.addComment(postId, "Hello");

    assertFalse(ok);
    assertTrue(comments.findByPost(postId).isEmpty());
  }

  @Test
  void addComment_persistsAndLists() {
    var comments = new InMemoryCommentRepository();
    var session = new Session();
    session.setUser(new User("stud01", "pw", Role.STUDENT));
    var bus = new EventBus();
    var ctl = new CommentController(comments, session, bus);
    var postId = UUID.randomUUID();

    boolean ok = ctl.addComment(postId, "Looks good!");

    assertTrue(ok);
    assertEquals(1, comments.findByPost(postId).size());
    assertEquals("Looks good!", comments.findByPost(postId).get(0).body());
  }
}
