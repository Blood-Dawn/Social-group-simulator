package org.campusboard.sgs.filter;

import java.util.List;
import org.campusboard.sgs.model.Post;

public interface FilterStrategy {
    List<Post> apply(List<Post> posts);
    String getName();
}
