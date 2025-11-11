package org.campusboard.sgs.filter;

import org.campusboard.sgs.model.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter that returns all posts (no filtering)
 */
public class AllFilter implements FilterStrategy {
    @Override
    public List<Post> apply(List<Post> posts) {
        return new ArrayList<>(posts);
    }
}
