package org.campusboard.sgs.filter;

import java.util.ArrayList;
import java.util.List;
import org.campusboard.sgs.model.Category;
import org.campusboard.sgs.model.Post;

public class CategoryFilter implements FilterStrategy {

    private final Category category;

    public CategoryFilter(Category category) {
        this.category = category;
    }

    @Override
    public List<Post> apply(List<Post> posts) {
        List<Post> filtered = new ArrayList<>();
        for (Post p : posts) {
            if (p.getCategory() == category) {
                filtered.add(p);
            }
        }
        return filtered;
    }

    @Override
    public String getName() {
        return "Category(" + category.name() + ")";
    }
}