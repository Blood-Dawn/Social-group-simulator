package org.campusboard.sgs.repo;

import org.campusboard.sgs.model.*;
import java.util.*;

public class InMemoryUserRepository implements UserRepository {
  private final Map<String,User> users = new HashMap<>();
  public InMemoryUserRepository() {
    add(new User("admin","admin123", Role.ADMIN));
    add(new User("staff01","staff123", Role.STAFF));
    add(new User("stud01","stud123", Role.STUDENT));
  }
  @Override public Optional<User> find(String username){ return Optional.ofNullable(users.get(username)); }
  @Override public void add(User u){ users.put(u.username(), u); }
}
