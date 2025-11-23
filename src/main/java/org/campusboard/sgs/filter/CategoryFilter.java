package org.campusboard.sgs.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.campusboard.sgs.model.Category;
import org.campusboard.sgs.model.Post;

public class CategoryFilter implements FilterStrategy {

    private final Category category;

    public CategoryFilter(Category category) {
        this.category = Objects.requireNonNull(category, "category must not be null");
    }

    @Override
    public java.util.stream.Stream<Post> filter(List<Post> posts) {
        List<Post> filtered = new ArrayList<>();
        for (Post p : posts) {
            if (p.category() == category) {
                filtered.add(p);
            }
        }
        return filtered.stream();
    }

    @Override
    public String getDescription() {
        return category.name();
    }
}
