package org.campusboard.sgs.filter;

import org.campusboard.sgs.model.Post;
import java.util.List;

public interface FilterStrategy {
    List<Post> apply(List<Post> posts);
}
