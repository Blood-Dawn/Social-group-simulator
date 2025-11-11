package org.campusboard.sgs.filter;

import org.campusboard.sgs.model.*;
import java.util.List;
import java.util.stream.Stream;

public class CategoryFilter implements FilterStrategy {
  private final Category category;

  public CategoryFilter(Category category) {
    this.category = category;
  }

  @Override
  public Stream<Post> filter(List<Post> posts) {
    return posts.stream().filter(p -> p.category() == category);
  }

  @Override
  public String getDescription() {
    return category.name();
  }

  public Category getCategory() {
    return category;
  }
}
