package org.campusboard.sgs.controller;

import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.repo.PostRepository;
import org.campusboard.sgs.util.*;

public class DislikePostCommand implements Command {
  private final PostRepository repo;
  private final EventBus bus;
  private final Post post;
  private final String userId;
  private final boolean wasDisliked;

  public DislikePostCommand(PostRepository repo, EventBus bus, Post post, String userId) {
    this.repo = repo;
    this.bus = bus;
    this.post = post;
    this.userId = userId;
    this.wasDisliked = post.isDislikedBy(userId);
  }

  @Override
  public void execute() {
    post.toggleDislike(userId);
    repo.update(post);
    bus.publish(Events.POST_UPDATED, post.id());
  }

  @Override
  public void undo() {
    post.toggleDislike(userId);
    repo.update(post);
    bus.publish(Events.POST_UPDATED, post.id());
  }

  @Override
  public String getDescription() {
    return (wasDisliked ? "Remove dislike from" : "Dislike") + " post: " + post.title();
  }
}
