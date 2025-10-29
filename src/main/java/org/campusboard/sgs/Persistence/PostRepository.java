package org.campusboard.sgs.persistence;

import org.campusboard.sgs.model.Post;
import java.util.*;

public interface PostRepository {
    // CRUD Operations
    List<Post> findAll();
    Optional<Post> findById(UUID id);
    Post save(Post post);
    Post update(Post post);
    boolean delete(UUID id);
    
    // Social Interactions
    Post likePost(UUID id);
    Post dislikePost(UUID id);
}
