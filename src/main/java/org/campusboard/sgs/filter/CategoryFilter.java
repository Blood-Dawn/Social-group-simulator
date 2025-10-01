package org.campusboard.sgs.filter;

import org.campusboard.sgs.model.Category;
import org.campusboard.sgs.model.Post;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Filter posts by category.
 */
public class CategoryFilter implements FilterStrategy {
    private final Category category;
    
    public CategoryFilter(Category category) {
        this.category = category;
    }
    
    @Override
    public List<Post> apply(List<Post> posts) {
        return posts.stream()
                .filter(post -> post.getCategory() == category)
                .collect(Collectors.toList());
    }
}
