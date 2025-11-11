package org.campusboard.sgs.filter;

import org.campusboard.sgs.model.Post;
import java.util.*;
import java.util.stream.Stream;

public class TrendingFilter implements FilterStrategy {
  @Override
  public Stream<Post> filter(List<Post> posts) {
    return posts.stream()
        .sorted(Comparator.comparingInt(Post::likeCount).reversed());
  }

  @Override
  public String getDescription() {
    return "Trending";
  }
}
