package org.campusboard.sgs;

import org.campusboard.sgs.controller.*;
import org.campusboard.sgs.repo.*;
import org.campusboard.sgs.util.*;
import org.campusboard.sgs.model.*;
import org.campusboard.sgs.filter.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FilterTest {
  @Test
  void lostAndFoundAppears() {
    var ctl = new PostController(new InMemoryPostRepository(), new InMemoryUserRepository(), new Session(),
        new EventBus());
    ctl.setFilter(new CategoryFilter(Category.LOST_FOUND));
    assertTrue(ctl.current().stream().allMatch(p -> p.category() == Category.LOST_FOUND));
  }
}
