package org.campusboard.sgs.filter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.campusboard.sgs.model.Post;
// Zach

/**
 * Sorts posts newest-first based on createdAt.
 */
public class SortByNew implements FilterStrategy {
  @Override
  public Stream<Post> filter(List<Post> input) {
    return input.stream()
        .sorted(Comparator.comparing(Post::createdAt,
            Comparator.nullsLast(Comparator.reverseOrder())));
  }

  @Override
  public String getDescription() {
    return "SortByNew";
  }
}
