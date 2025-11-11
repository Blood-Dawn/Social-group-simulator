package org.campusboard.sgs.controller;

import org.campusboard.sgs.Persistence.PostRepository;
import org.campusboard.sgs.model.Category;
import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.model.User;

import java.util.UUID;

/**
 * Command to create a new post with undo support
 */
public class CreatePostCommand implements Command {
    private final PostRepository repository;
    private final String title;
    private final String body;
    private final Category category;
    private final User author;
    private Post createdPost;

    public CreatePostCommand(PostRepository repository, String title, String body, Category category, User author) {
        this.repository = repository;
        this.title = title;
        this.body = body;
        this.category = category;
        this.author = author;
    }

    @Override
    public void execute() {
        createdPost = new Post(title, body, category, author);
        repository.save(createdPost);
        EventBus.publish(AppEvent.POST_CREATED, createdPost);
        EventBus.publish(AppEvent.POSTS_CHANGED);
    }

    @Override
    public void undo() {
        if (createdPost != null) {
            repository.delete(createdPost.getId());
            EventBus.publish(AppEvent.POST_DELETED, createdPost.getId());
            EventBus.publish(AppEvent.POSTS_CHANGED);
        }
    }
}
