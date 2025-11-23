package org.campusboard.sgs.filter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.campusboard.sgs.model.Post;

public class TrendingFilter implements FilterStrategy {
  @Override
  public Stream<Post> filter(List<Post> posts) {
    return posts.stream()
        .sorted(Comparator.comparingInt(Post::likeCount).reversed()
            .thenComparing(Post::createdAt, Comparator.nullsLast(Comparator.reverseOrder())));
  }

  @Override
  public String getDescription() {
    return "Trending";
  }
}
