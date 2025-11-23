package org.campusboard.sgs.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.campusboard.sgs.filter.CategoryFilter;
import org.campusboard.sgs.filter.SortByNew;
import org.campusboard.sgs.model.Category;
import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.model.Role;
import org.campusboard.sgs.model.User;
import org.campusboard.sgs.repo.PostRepository;
import org.campusboard.sgs.repo.InMemoryUserRepository;
import org.campusboard.sgs.util.EventBus;
import org.campusboard.sgs.util.Session;
import org.junit.jupiter.api.Test;

class PostControllerTest {

  @Test
  void current_respectsFilterAndSort() {
    var repo = new TestPostRepository();
    var users = new InMemoryUserRepository();
    var session = new Session();
    var bus = new EventBus();
    var controller = new PostController(repo, users, session, bus);

    LocalDateTime now = LocalDateTime.now();
    Post oldLost = new Post(UUID.randomUUID(), "Old Lost", "Body", Category.LOST_FOUND, "student",
        now.minusDays(1), java.util.Set.of());
    Post newLost = new Post(UUID.randomUUID(), "New Lost", "Body", Category.LOST_FOUND, "student",
        now, java.util.Set.of());
    Post event = new Post(UUID.randomUUID(), "Event", "Body", Category.EVENTS, "staff",
        now.minusHours(2), java.util.Set.of());

    repo.save(oldLost);
    repo.save(newLost);
    repo.save(event);

    controller.setFilter(new CategoryFilter(Category.LOST_FOUND));
    controller.setSort(new SortByNew());

    List<Post> result = controller.current();

    assertEquals(2, result.size());
    assertEquals(newLost.id(), result.get(0).id());
    assertEquals(oldLost.id(), result.get(1).id());
  }

  @Test
  void toggleLike_addsAndRemovesPerUser() {
    var repo = new TestPostRepository();
    var users = new InMemoryUserRepository();
    var session = new Session();
    var bus = new EventBus();
    var controller = new PostController(repo, users, session, bus);

    Post post = new Post(null, "Title", "Body", Category.EVENTS, "student");
    repo.save(post);

    session.setUser(new User("student", "pw", Role.STUDENT));

    controller.toggleLike(post);
    assertTrue(post.isLikedBy("student"));
    controller.toggleLike(post);
    assertFalse(post.isLikedBy("student"));
  }

  private static class TestPostRepository implements PostRepository {
    private final Map<UUID, Post> store = new HashMap<>();

    @Override
    public List<Post> findAll() {
      return List.copyOf(store.values());
    }

    @Override
    public Optional<Post> findById(UUID id) {
      return Optional.ofNullable(store.get(id));
    }

    @Override
    public void save(Post p) {
      store.put(p.id(), p);
    }

    @Override
    public void update(Post p) {
      store.put(p.id(), p);
    }

    @Override
    public void delete(UUID id) {
      store.remove(id);
    }

    @Override
    public List<Post> find(Category category, String search) {
      return store.values().stream().filter(p -> {
        boolean categoryOk = category == null || p.category() == category;
        boolean searchOk = search == null || search.isBlank()
            || p.title().toLowerCase().contains(search.toLowerCase())
            || p.body().toLowerCase().contains(search.toLowerCase());
        return categoryOk && searchOk;
      }).toList();
    }
  }
}
