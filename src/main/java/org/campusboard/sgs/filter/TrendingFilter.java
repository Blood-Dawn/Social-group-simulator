package org.campusboard.sgs.filter;

import org.campusboard.sgs.model.Post;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Filter that returns only trending posts (posts with score above threshold)
 */
public class TrendingFilter implements FilterStrategy {
    private final int threshold;

    public TrendingFilter(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public List<Post> apply(List<Post> posts) {
        return posts.stream()
                .filter(post -> getScore(post) >= threshold)
                .collect(Collectors.toList());
    }

    private int getScore(Post post) {
        return post.getLikes() - post.getDislikes();
    }
}
