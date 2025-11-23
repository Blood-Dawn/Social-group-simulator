package org.campusboard.sgs.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.stream.Collectors;
import org.campusboard.sgs.model.Category;
import org.campusboard.sgs.repo.InMemoryCommentRepository;
import org.campusboard.sgs.repo.InMemoryPostRepository;
import org.campusboard.sgs.repo.InMemoryUserRepository;
import org.junit.jupiter.api.Test;

class DemoDataSeederTest {

  @Test
  void seedsAtLeastTenPerCategory() {
    var posts = new InMemoryPostRepository();
    var users = new InMemoryUserRepository();

    DemoDataSeeder.ensureDemoData(posts, users, 42L);

    Map<Category, Long> counts = posts.findAll().stream()
        .collect(Collectors.groupingBy(p -> p.category(), Collectors.counting()));

    assertEquals(4, counts.size());
    for (Category c : Category.values()) {
      assertTrue(counts.getOrDefault(c, 0L) >= 10, "Expected >= 10 posts for " + c);
    }
  }

  @Test
  void seedsCommentsWhenEmpty() {
    var posts = new InMemoryPostRepository();
    var users = new InMemoryUserRepository();
    DemoDataSeeder.ensureDemoData(posts, users, 42L);

    var comments = new InMemoryCommentRepository();
    DemoDataSeeder.ensureDemoComments(comments, posts, 84L);

    long withComments = posts.findAll().stream().filter(p -> !comments.findByPost(p.id()).isEmpty()).count();
    assertTrue(withComments > 0, "Expected at least one seeded comment");
    assertTrue(withComments <= posts.findAll().size(), "Should not exceed total posts");
  }

  @Test
  void seederIsIdempotent() {
    var posts = new InMemoryPostRepository();
    var users = new InMemoryUserRepository();
    DemoDataSeeder.ensureDemoData(posts, users, 99L);
    int firstCount = posts.findAll().size();
    DemoDataSeeder.ensureDemoData(posts, users, 99L);
    assertEquals(firstCount, posts.findAll().size(), "Second seeding should not duplicate posts");
  }

  @Test
  void adminPostsPrefixedTest() {
    var posts = new InMemoryPostRepository();
    var users = new InMemoryUserRepository();
    DemoDataSeeder.ensureDemoData(posts, users, 11L);
    assertTrue(posts.findAll().stream()
        .filter(p -> "admin".equalsIgnoreCase(p.author()))
        .allMatch(p -> p.title().startsWith("[TEST]")), "Admin posts should be [TEST] prefixed");
    assertTrue(posts.findAll().stream()
        .filter(p -> p.author() != null && p.author().toLowerCase().startsWith("staff"))
        .noneMatch(p -> p.title().startsWith("[TEST]")), "Staff posts should not be [TEST] prefixed");
  }
}
