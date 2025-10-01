package org.campusboard.sgs.Persistence;

import org.campusboard.sgs.model.Post;
import java.util.*;

public interface PostRepository {
    List<Post> findAll();
    Optional<Post> findById(UUID id);
    boolean delete(UUID id);
    Post save(Post post);
    Post update(Post post);
    Post likePost(UUID id);
    Post dislikePost(UUID id);
    Post deletePost(UUID id);
}
