package org.campusboard.sgs.repo;

import org.campusboard.sgs.model.*;
import java.util.*;

public interface UserRepository {
  Optional<User> find(String username);
  void add(User u);

  default java.util.Collection<User> listAll() {
    return java.util.List.of();
  }

  default boolean validatePassword(String username, String password) {
    return find(username).map(User::password).map(pw -> pw.equals(password)).orElse(false);
  }

  default void assignPassword(String username, String password) {
    // no-op default; repositories may override
  }
}
