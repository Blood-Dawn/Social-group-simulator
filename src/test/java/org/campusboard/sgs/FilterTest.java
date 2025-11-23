package org.campusboard.sgs;

import org.campusboard.sgs.controller.*;
import org.campusboard.sgs.demo.DemoDataSeeder;
import org.campusboard.sgs.repo.*;
import org.campusboard.sgs.util.*;
import org.campusboard.sgs.model.*;
import org.campusboard.sgs.filter.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FilterTest {
  @Test
  void lostAndFoundAppears() {
    var posts = new InMemoryPostRepository();
    var users = new InMemoryUserRepository();
    DemoDataSeeder.ensureDemoData(posts, users, 7L);
    var ctl = new PostController(posts, users, new Session(), new EventBus());
    ctl.setFilter(new CategoryFilter(Category.LOST_FOUND));
    var filtered = ctl.current();
    assertFalse(filtered.isEmpty(), "Expected seeded lost & found posts");
    assertTrue(filtered.stream().allMatch(p -> p.category() == Category.LOST_FOUND));
  }
}
