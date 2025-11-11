package org.campusboard.sgs.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {
  @Test
  void toggleLike_firstToggle_addsLike() {
    var post = new Post(null, "Test Title", "Test Body", Category.ANNOUNCEMENTS, "testuser");

    boolean result = post.toggleLike("user1");

    assertTrue(result, "First toggle should return true (liked)");
    assertEquals(1, post.likeCount(), "Like count should be 1");
    assertTrue(post.isLikedBy("user1"), "Post should be liked by user1");
  }

  @Test
  void toggleLike_secondToggle_removesLike() {
    var post = new Post(null, "Test Title", "Test Body", Category.ANNOUNCEMENTS, "testuser");
    post.toggleLike("user1");

    boolean result = post.toggleLike("user1");

    assertFalse(result, "Second toggle should return false (unliked)");
    assertEquals(0, post.likeCount(), "Like count should be 0");
    assertFalse(post.isLikedBy("user1"), "Post should not be liked by user1");
  }

  @Test
  void toggleLike_multipleDifferentUsers_countsCorrectly() {
    var post = new Post(null, "Test Title", "Test Body", Category.ANNOUNCEMENTS, "testuser");

    post.toggleLike("user1");
    post.toggleLike("user2");
    post.toggleLike("user3");

    assertEquals(3, post.likeCount(), "Like count should be 3");
    assertTrue(post.isLikedBy("user1"));
    assertTrue(post.isLikedBy("user2"));
    assertTrue(post.isLikedBy("user3"));
  }

  @Test
  void toggleLike_unlikeOneOfMany_decreasesCount() {
    var post = new Post(null, "Test Title", "Test Body", Category.ANNOUNCEMENTS, "testuser");
    post.toggleLike("user1");
    post.toggleLike("user2");
    post.toggleLike("user3");

    post.toggleLike("user2");

    assertEquals(2, post.likeCount(), "Like count should be 2");
    assertTrue(post.isLikedBy("user1"));
    assertFalse(post.isLikedBy("user2"));
    assertTrue(post.isLikedBy("user3"));
  }

  @Test
  void setTitle_updatesTitle() {
    var post = new Post(null, "Old Title", "Body", Category.ANNOUNCEMENTS, "testuser");

    post.setTitle("New Title");

    assertEquals("New Title", post.title());
  }

  @Test
  void setCategory_updatesCategory() {
    var post = new Post(null, "Title", "Body", Category.ANNOUNCEMENTS, "testuser");

    post.setCategory(Category.EVENTS);

    assertEquals(Category.EVENTS, post.category());
  }
}
