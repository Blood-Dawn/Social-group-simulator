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

    boolean any = posts.findAll().stream().anyMatch(p -> !comments.findByPost(p.id()).isEmpty());
    assertTrue(any, "Expected at least one seeded comment");
  }
}
