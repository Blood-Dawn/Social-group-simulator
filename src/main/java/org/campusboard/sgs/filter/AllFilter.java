package org.campusboard.sgs.filter;

import org.campusboard.sgs.model.Post;
import java.util.List;
import java.util.stream.Stream;

public class AllFilter implements FilterStrategy {
  @Override
  public Stream<Post> filter(List<Post> posts) {
    return posts.stream();
  }

  @Override
  public String getDescription() {
    return "All Posts";
  }
}
