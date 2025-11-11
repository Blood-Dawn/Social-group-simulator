package org.campusboard.sgs.controller;

import org.campusboard.sgs.Persistence.PostRepository;
import org.campusboard.sgs.model.Post;

import java.util.UUID;

/**
 * Command to like a post with undo support
 */
public class LikePostCommand implements Command {
    private final PostRepository repository;
    private final UUID postId;

    public LikePostCommand(PostRepository repository, UUID postId) {
        this.repository = repository;
        this.postId = postId;
    }

    @Override
    public void execute() {
        Post post = repository.likePost(postId);
        if (post != null) {
            EventBus.publish(AppEvent.POST_LIKED, post);
            EventBus.publish(AppEvent.POSTS_CHANGED);
        }
    }

    @Override
    public void undo() {
        // Undo by decrementing the like count
        Post post = repository.findById(postId).orElse(null);
        if (post != null && post.getLikes() > 0) {
            post.setLikes(post.getLikes() - 1);
            repository.update(post);
            EventBus.publish(AppEvent.POST_UPDATED, post);
            EventBus.publish(AppEvent.POSTS_CHANGED);
        }
    }
}
