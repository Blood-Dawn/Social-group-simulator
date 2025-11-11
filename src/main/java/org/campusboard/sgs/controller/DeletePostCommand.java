package org.campusboard.sgs.controller;

import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.repo.PostRepository;
import org.campusboard.sgs.util.*;

public class DeletePostCommand implements Command {
  private final PostRepository repo;
  private final EventBus bus;
  private final Post post;

  public DeletePostCommand(PostRepository repo, EventBus bus, Post post) {
    this.repo = repo;
    this.bus = bus;
    this.post = post;
  }

  @Override
  public void execute() {
    repo.delete(post.id());
    bus.publish(Events.POSTS_REPLACED, null);
  }

  @Override
  public void undo() {
    repo.save(post);
    bus.publish(Events.POSTS_REPLACED, null);
  }

  @Override
  public String getDescription() {
    return "Delete post: " + post.title();
  }
}
