package org.campusboard.sgs.filter;

import org.campusboard.sgs.model.*;
import org.campusboard.sgs.repo.InMemoryUserRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class FilterStrategyTest {
  private List<Post> posts;

  @BeforeEach
  void setUp() {
    posts = new ArrayList<>();
    posts.add(new Post(null, "Announcement 1", "Body", Category.ANNOUNCEMENTS, "user1"));
    posts.add(new Post(null, "Event 1", "Body", Category.EVENTS, "user2"));
    posts.add(new Post(null, "Study Group", "Body", Category.STUDY_GROUPS, "user3"));
    posts.add(new Post(null, "Lost Item", "Body", Category.LOST_FOUND, "user1"));
    posts.add(new Post(null, "Announcement 2", "Body", Category.ANNOUNCEMENTS, "user2"));

    // Add some likes to test trending
    posts.get(1).toggleLike("user1");
    posts.get(1).toggleLike("user2");
    posts.get(1).toggleLike("user3"); // Event 1 has 3 likes
    posts.get(0).toggleLike("user1"); // Announcement 1 has 1 like
  }

  @Test
  void allFilter_returnsAllPosts() {
    FilterStrategy filter = new AllFilter();

    List<Post> result = filter.filter(posts).toList();

    assertEquals(5, result.size(), "AllFilter should return all posts");
  }

  @Test
  void categoryFilter_announcements_returnsOnlyAnnouncements() {
    FilterStrategy filter = new CategoryFilter(Category.ANNOUNCEMENTS);

    List<Post> result = filter.filter(posts).toList();

    assertEquals(2, result.size(), "Should return 2 announcements");
    assertTrue(result.stream().allMatch(p -> p.category() == Category.ANNOUNCEMENTS));
  }

  @Test
  void categoryFilter_events_returnsOnlyEvents() {
    FilterStrategy filter = new CategoryFilter(Category.EVENTS);

    List<Post> result = filter.filter(posts).toList();

    assertEquals(1, result.size(), "Should return 1 event");
    assertEquals(Category.EVENTS, result.get(0).category());
  }

  @Test
  void categoryFilter_lostFound_returnsOnlyLostFound() {
    FilterStrategy filter = new CategoryFilter(Category.LOST_FOUND);

    List<Post> result = filter.filter(posts).toList();

    assertEquals(1, result.size(), "Should return 1 lost & found post");
    assertEquals(Category.LOST_FOUND, result.get(0).category());
  }

  @Test
  void trendingFilter_sortsByLikeCountDescending() {
    FilterStrategy filter = new TrendingFilter();

    List<Post> result = filter.filter(posts).toList();

    assertEquals(5, result.size(), "Should return all posts");
    // Event 1 (3 likes) should be first
    assertEquals("Event 1", result.get(0).title());
    assertEquals(3, result.get(0).likeCount());
    // Announcement 1 (1 like) should be second
    assertEquals("Announcement 1", result.get(1).title());
    assertEquals(1, result.get(1).likeCount());
  }

  @Test
  void categoryFilter_getDescription_returnsCorrectName() {
    CategoryFilter filter = new CategoryFilter(Category.ANNOUNCEMENTS);

    assertEquals("ANNOUNCEMENTS", filter.getDescription());
  }

  @Test
  void allFilter_getDescription_returnsAllPosts() {
    AllFilter filter = new AllFilter();

    assertEquals("All Posts", filter.getDescription());
  }

  @Test
  void trendingFilter_getDescription_returnsTrending() {
    TrendingFilter filter = new TrendingFilter();

    assertEquals("Trending", filter.getDescription());
  }

  @Test
  void authorTypeFilter_filtersByUserType() {
    var users = new InMemoryUserRepository();
    users.add(new User("studX", "pw", Role.STUDENT));
    users.add(new User("staffX", "pw", Role.STAFF));

    List<Post> sample = List.of(
        new Post(null, "Student Post", "Body", Category.EVENTS, "studX"),
        new Post(null, "Staff Post", "Body", Category.ANNOUNCEMENTS, "staffX"));

    FilterStrategy filter = new AuthorTypeFilter(UserType.STAFF, users);
    List<Post> result = filter.filter(sample).toList();

    assertEquals(1, result.size());
    assertEquals("Staff Post", result.get(0).title());
  }

  @Test
  void sortByNew_ordersNewestFirst() {
    LocalDateTime now = LocalDateTime.now();
    List<Post> sample = List.of(
        new Post(UUID.randomUUID(), "Older", "Body", Category.EVENTS, "user1", now.minusDays(2), Set.of()),
        new Post(UUID.randomUUID(), "Newest", "Body", Category.EVENTS, "user2", now, Set.of()),
        new Post(UUID.randomUUID(), "Middle", "Body", Category.EVENTS, "user3", now.minusHours(5), Set.of()));

    FilterStrategy sorter = new SortByNew();
    List<Post> result = sorter.filter(sample).toList();

    assertEquals("Newest", result.get(0).title());
    assertEquals("Middle", result.get(1).title());
    assertEquals("Older", result.get(2).title());
  }
}
