package org.campusboard.sgs;

import org.campusboard.sgs.repo.*;
import org.campusboard.sgs.util.*;
import org.campusboard.sgs.controller.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthTest {
  @Test void adminLoginWorks(){
    var auth = new AuthController(new InMemoryUserRepository(), new Session(), new EventBus());
    assertTrue(auth.login("admin","admin123"));
  }
  @Test void wrongPassFails(){
    var auth = new AuthController(new InMemoryUserRepository(), new Session(), new EventBus());
    assertFalse(auth.login("admin","nope"));
  }
}
