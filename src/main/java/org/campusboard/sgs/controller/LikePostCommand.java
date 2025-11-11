package org.campusboard.sgs.controller;

import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.repo.PostRepository;
import org.campusboard.sgs.util.*;

public class LikePostCommand implements Command {
  private final PostRepository repo;
  private final EventBus bus;
  private final Post post;
  private final String userId;
  private final boolean wasLiked;

  public LikePostCommand(PostRepository repo, EventBus bus, Post post, String userId) {
    this.repo = repo;
    this.bus = bus;
    this.post = post;
    this.userId = userId;
    this.wasLiked = post.isLikedBy(userId);
  }

  @Override
  public void execute() {
    post.toggleLike(userId);
    repo.update(post);
    bus.publish(Events.POST_UPDATED, post.id());
  }

  @Override
  public void undo() {
    post.toggleLike(userId);
    repo.update(post);
    bus.publish(Events.POST_UPDATED, post.id());
  }

  @Override
  public String getDescription() {
    return (wasLiked ? "Unlike" : "Like") + " post: " + post.title();
  }
}
