package org.campusboard.sgs.filter;

import java.util.List;
import java.util.stream.Stream;
import org.campusboard.sgs.model.Post;
// Zach

/**
 * Strategy interface for filtering/sorting post collections.
 * Implementations may filter or reorder the provided list.
 */
public interface FilterStrategy {
  Stream<Post> filter(List<Post> posts);
  String getDescription();
}
