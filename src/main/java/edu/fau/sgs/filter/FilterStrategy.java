package edu.fau.sgs.filter;

import edu.fau.sgs.model.Post;
import java.util.List;

public interface FilterStrategy {
    List<Post> apply(List<Post> posts);
}
