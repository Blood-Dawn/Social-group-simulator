package org.campusboard.sgs.filter;

import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.model.UserType;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Filter posts by author's user type (e.g., STAFF, STUDENT, CLUB)
 */
public class AuthorTypeFilter implements FilterStrategy {
    private final UserType userType;

    public AuthorTypeFilter(UserType userType) {
        this.userType = userType;
    }

    @Override
    public List<Post> apply(List<Post> posts) {
        return posts.stream()
                .filter(post -> post.getAuthor() != null && post.getAuthor().getUserType() == userType)
                .collect(Collectors.toList());
    }
}
