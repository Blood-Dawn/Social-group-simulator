package org.campusboard.sgs.filter;

import org.campusboard.sgs.model.Post;
import java.util.List;
import java.util.stream.Stream;

public interface FilterStrategy {
  Stream<Post> filter(List<Post> posts);
  String getDescription();
}
