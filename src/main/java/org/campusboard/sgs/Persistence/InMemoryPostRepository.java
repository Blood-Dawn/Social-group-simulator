package org.campusboard.sgs.Persistence;

import org.campusboard.sgs.model.Post;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPostRepository implements PostRepository {

    private final Map<UUID, Post> posts = new ConcurrentHashMap<>();

    @Override
    public List<Post> findAll() {
        return new ArrayList<>(posts.values());
    }

    @Override
    public Optional<Post> findById(UUID id) {
        return Optional.ofNullable(posts.get(id));
    }

    @Override
    public boolean delete(UUID id) {
        return posts.remove(id) != null;
    }

    @Override
    public Post save(Post post) {
        Objects.requireNonNull(post, "Post cannot be null");
        Objects.requireNonNull(post.getAuthor(), "Post author cannot be null");
        posts.put(post.getId(), post);
        return post;
    }

    @Override
    public Post update(Post post) {
        if (post == null || post.getId() == null || !posts.containsKey(post.getId())) {
            return null; // Post doesn't exist
        }
        Objects.requireNonNull(post.getAuthor(), "Post author cannot be null");
        posts.put(post.getId(), post);
        return post;
    }

    @Override
    public Post likePost(UUID id) {
        Post post = posts.get(id);
        if (post != null) {
            post.setLikes(post.getLikes() + 1);
        }
        return post;
    }

    @Override
    public Post dislikePost(UUID id) {
        Post post = posts.get(id);
        if (post != null) {
            post.setDislikes(post.getDislikes() + 1);
        }
        return post;
    }
}
