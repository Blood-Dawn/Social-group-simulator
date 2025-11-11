package org.campusboard.sgs.repo;

import org.campusboard.sgs.model.*;
import java.util.*;

public interface PostRepository {
  List<Post> findAll();
  Optional<Post> findById(UUID id);
  void save(Post p);
  void update(Post p);
  void delete(UUID id);
  List<Post> find(Category category, String search);
}
