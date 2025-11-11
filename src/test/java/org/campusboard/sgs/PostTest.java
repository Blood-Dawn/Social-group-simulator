package org.campusboard.sgs;

import org.campusboard.sgs.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {
  @Test void toggleTwice_likeThenUnlike(){
    var p = new Post(null,"t","b", Category.ANNOUNCEMENTS,"stud01");
    assertTrue(p.toggleLike("stud01"));
    assertEquals(1, p.likeCount());
    assertFalse(p.toggleLike("stud01"));
    assertEquals(0, p.likeCount());
  }
}
