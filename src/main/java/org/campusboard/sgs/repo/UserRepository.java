package org.campusboard.sgs.repo;

import org.campusboard.sgs.model.*;
import java.util.*;

public interface UserRepository {
  Optional<User> find(String username);
  void add(User u);
}
