package org.campusboard.sgs.controller;

import org.campusboard.sgs.model.*;
import org.campusboard.sgs.repo.InMemoryUserRepository;
import org.campusboard.sgs.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {
  private AuthController authController;
  private InMemoryUserRepository userRepo;
  private Session session;
  private EventBus bus;

  @BeforeEach
  void setUp() {
    userRepo = new InMemoryUserRepository();
    session = new Session();
    bus = new EventBus();
    authController = new AuthController(userRepo, session, bus);

    // Seed test users
    userRepo.add(new User("testuser", "testpass", Role.STUDENT));
    userRepo.add(new User("admin", "adminpass", Role.ADMIN));
  }

  @Test
  void login_validCredentials_returnsTrue() {
    boolean result = authController.login("testuser", "testpass");

    assertTrue(result, "Login should succeed with valid credentials");
    assertTrue(session.isAuthenticated(), "Session should be authenticated");
    assertEquals("testuser", session.user().username());
  }

  @Test
  void login_invalidPassword_returnsFalse() {
    boolean result = authController.login("testuser", "wrongpass");

    assertFalse(result, "Login should fail with invalid password");
    assertFalse(session.isAuthenticated(), "Session should not be authenticated");
  }

  @Test
  void login_nonexistentUser_returnsFalse() {
    boolean result = authController.login("nonexistent", "anypass");

    assertFalse(result, "Login should fail with nonexistent user");
    assertFalse(session.isAuthenticated(), "Session should not be authenticated");
  }

  @Test
  void logout_clearsSession() {
    authController.login("testuser", "testpass");
    assertTrue(session.isAuthenticated(), "Should be authenticated before logout");

    authController.logout();

    assertFalse(session.isAuthenticated(), "Session should not be authenticated after logout");
  }

  @Test
  void login_publishesEvent() {
    final boolean[] eventReceived = {false};
    bus.subscribe(Events.USER_LOGGED_IN, e -> eventReceived[0] = true);

    authController.login("testuser", "testpass");

    assertTrue(eventReceived[0], "USER_LOGGED_IN event should be published");
  }

  @Test
  void logout_publishesEvent() {
    final boolean[] eventReceived = {false};
    authController.login("testuser", "testpass");
    bus.subscribe(Events.USER_LOGGED_OUT, e -> eventReceived[0] = true);

    authController.logout();

    assertTrue(eventReceived[0], "USER_LOGGED_OUT event should be published");
  }
}
