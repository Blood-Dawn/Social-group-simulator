package org.campusboard.sgs.filter;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import org.campusboard.sgs.model.Post;

/**
 * Filters posts whose author username contains the provided substring.
 */
public class AuthorFilter implements FilterStrategy {
  private final String needle;

  public AuthorFilter(String authorContains) {
    this.needle = authorContains == null ? "" : authorContains.trim().toLowerCase(Locale.ENGLISH);
  }

  @Override
  public Stream<Post> filter(List<Post> input) {
    if (needle.isEmpty()) {
      return input.stream();
    }
    return input.stream()
        .filter(p -> p.author() != null && p.author().toLowerCase(Locale.ENGLISH).contains(needle));
  }

  @Override
  public String getDescription() {
    return "Author(" + needle + ")";
  }
}
