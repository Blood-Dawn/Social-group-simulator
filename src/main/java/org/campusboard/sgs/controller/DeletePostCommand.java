package org.campusboard.sgs.controller;

import org.campusboard.sgs.Persistence.PostRepository;
import org.campusboard.sgs.model.Post;

import java.util.UUID;

/**
 * Command to delete a post with undo support
 */
public class DeletePostCommand implements Command {
    private final PostRepository repository;
    private final UUID postId;
    private Post deletedPost;

    public DeletePostCommand(PostRepository repository, UUID postId) {
        this.repository = repository;
        this.postId = postId;
    }

    @Override
    public void execute() {
        deletedPost = repository.findById(postId).orElse(null);
        if (deletedPost != null) {
            repository.delete(postId);
            EventBus.publish(AppEvent.POST_DELETED, postId);
            EventBus.publish(AppEvent.POSTS_CHANGED);
        }
    }

    @Override
    public void undo() {
        if (deletedPost != null) {
            repository.save(deletedPost);
            EventBus.publish(AppEvent.POST_CREATED, deletedPost);
            EventBus.publish(AppEvent.POSTS_CHANGED);
        }
    }
}
