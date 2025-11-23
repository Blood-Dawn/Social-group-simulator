package org.campusboard.sgs.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;
import org.campusboard.sgs.model.Category;
import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.model.Role;
import org.campusboard.sgs.model.User;
import org.campusboard.sgs.repo.InMemoryPostRepository;
import org.campusboard.sgs.repo.InMemoryUserRepository;
import org.campusboard.sgs.util.EventBus;
import org.campusboard.sgs.util.Session;
import org.junit.jupiter.api.Test;

class PostControllerVisibilityTest {

  @Test
  void hidesAdminPostsForNonAdmin() {
    var posts = new InMemoryPostRepository();
    var users = new InMemoryUserRepository();
    users.add(new User("admin_admin", "pw", Role.ADMIN));
    users.add(new User("alex_student", "pw", Role.STUDENT));
    posts.save(new Post(UUID.randomUUID(), "Admin Only", "Body", Category.ANNOUNCEMENTS, "admin_admin"));
    posts.save(new Post(UUID.randomUUID(), "Student Post", "Body", Category.ANNOUNCEMENTS, "alex_student"));

    var session = new Session();
    session.setUser(new User("alex_student", "pw", Role.STUDENT));
    var ctl = new PostController(posts, users, session, new EventBus());

    List<Post> visible = ctl.current();
    assertEquals(1, visible.size(), "Non-admin should not see admin posts");
    assertEquals("Student Post", visible.get(0).title());
  }

  @Test
  void showsAdminPostsForAdmin() {
    var posts = new InMemoryPostRepository();
    var users = new InMemoryUserRepository();
    users.add(new User("admin_admin", "pw", Role.ADMIN));
    users.add(new User("alex_student", "pw", Role.STUDENT));
    posts.save(new Post(UUID.randomUUID(), "Admin Only", "Body", Category.ANNOUNCEMENTS, "admin_admin"));
    posts.save(new Post(UUID.randomUUID(), "Student Post", "Body", Category.ANNOUNCEMENTS, "alex_student"));

    var session = new Session();
    session.setUser(new User("admin_admin", "pw", Role.ADMIN));
    var ctl = new PostController(posts, users, session, new EventBus());

    List<Post> visible = ctl.current();
    assertEquals(2, visible.size(), "Admin should see all posts including admin posts");
  }

  @Test
  void combinesCategoryAndAuthorFilters() {
    var posts = new InMemoryPostRepository();
    var users = new InMemoryUserRepository();
    users.add(new User("alex_student", "pw", Role.STUDENT));
    users.add(new User("miller_staff", "pw", Role.STAFF));
    users.add(new User("riley_student", "pw", Role.STUDENT));
    posts.save(new Post(UUID.randomUUID(), "Student Event", "Body", Category.EVENTS, "alex_student"));
    posts.save(new Post(UUID.randomUUID(), "Staff Event", "Body", Category.EVENTS, "miller_staff"));
    posts.save(new Post(UUID.randomUUID(), "Student Study", "Body", Category.STUDY_GROUPS, "riley_student"));

    var ctl = new PostController(posts, users, new Session(), new EventBus());
    ctl.setCategoryFilter(new org.campusboard.sgs.filter.CategoryFilter(Category.EVENTS));
    ctl.setAuthorFilter(new org.campusboard.sgs.filter.AuthorTypeFilter(org.campusboard.sgs.model.UserType.STAFF, users));

    List<Post> filtered = ctl.current();
    assertEquals(1, filtered.size(), "Expected only staff events");
    assertTrue(filtered.get(0).author().contains("staff"), "Author should be staff");
    assertEquals(Category.EVENTS, filtered.get(0).category());
  }
}
