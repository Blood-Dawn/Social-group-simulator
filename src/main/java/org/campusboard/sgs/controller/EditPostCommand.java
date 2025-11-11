package org.campusboard.sgs.controller;

import org.campusboard.sgs.Persistence.PostRepository;
import org.campusboard.sgs.model.Post;

import java.util.UUID;
import java.util.function.UnaryOperator;

/**
 * Command to edit an existing post with undo support
 */
public class EditPostCommand implements Command {
    private final PostRepository repository;
    private final UUID postId;
    private final UnaryOperator<Post> updater;
    private Post originalPost;
    private Post updatedPost;

    public EditPostCommand(PostRepository repository, UUID postId, UnaryOperator<Post> updater) {
        this.repository = repository;
        this.postId = postId;
        this.updater = updater;
    }

    @Override
    public void execute() {
        originalPost = repository.findById(postId).orElse(null);
        if (originalPost != null) {
            updatedPost = updater.apply(originalPost);
            repository.update(updatedPost);
            EventBus.publish(AppEvent.POST_UPDATED, updatedPost);
            EventBus.publish(AppEvent.POSTS_CHANGED);
        }
    }

    @Override
    public void undo() {
        if (originalPost != null) {
            repository.update(originalPost);
            EventBus.publish(AppEvent.POST_UPDATED, originalPost);
            EventBus.publish(AppEvent.POSTS_CHANGED);
        }
    }
}
