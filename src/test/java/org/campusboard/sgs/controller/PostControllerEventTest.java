package org.campusboard.sgs.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.campusboard.sgs.model.Category;
import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.model.Role;
import org.campusboard.sgs.model.User;
import org.campusboard.sgs.repo.InMemoryPostRepository;
import org.campusboard.sgs.repo.InMemoryUserRepository;
import org.campusboard.sgs.util.EventBus;
import org.campusboard.sgs.util.Events;
import org.campusboard.sgs.util.Session;
import org.junit.jupiter.api.Test;

class PostControllerEventTest {

  @Test
  void toggleLikePublishesPostUpdated() {
    var posts = new InMemoryPostRepository();
    var users = new InMemoryUserRepository();
    var session = new Session();
    var bus = new EventBus();
    var ctl = new PostController(posts, users, session, bus);
    Post p = new Post(UUID.randomUUID(), "Title", "Body", Category.EVENTS, "alex");
    posts.save(p);
    session.setUser(new User("alex", "pw", Role.STUDENT));

    AtomicReference<UUID> updated = new AtomicReference<>();
    bus.subscribe(Events.POST_UPDATED, e -> updated.set((UUID) e.data));

    ctl.toggleLike(p);

    assertTrue(p.isLikedBy("alex"));
    assertEquals(p.id(), updated.get());
  }

  @Test
  void createPublishesPostsReplaced() {
    var posts = new InMemoryPostRepository();
    var users = new InMemoryUserRepository();
    var session = new Session();
    var bus = new EventBus();
    var ctl = new PostController(posts, users, session, bus);
    session.setUser(new User("alex", "pw", Role.STUDENT));

    final boolean[] fired = {false};
    bus.subscribe(Events.POSTS_REPLACED, e -> fired[0] = true);

    ctl.create("Hello", "Body", Category.ANNOUNCEMENTS);

    assertTrue(fired[0], "POSTS_REPLACED should fire on create");
    assertFalse(posts.findAll().isEmpty(), "Post should be persisted");
  }

  @Test
  void createValidatesTitleAndBody() {
    var ctl = new PostController(new InMemoryPostRepository(), new InMemoryUserRepository(), new Session(),
        new EventBus());
    boolean threw = false;
    try {
      ctl.create("   ", "body", Category.ANNOUNCEMENTS);
    } catch (IllegalArgumentException e) {
      threw = true;
    }
    assertTrue(threw, "Expected blank title to throw");
  }
}
