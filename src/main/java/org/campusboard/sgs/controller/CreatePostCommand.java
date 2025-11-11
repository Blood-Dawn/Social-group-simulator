package org.campusboard.sgs.controller;

import org.campusboard.sgs.model.*;
import org.campusboard.sgs.repo.PostRepository;
import org.campusboard.sgs.util.*;
import java.util.UUID;

public class CreatePostCommand implements Command {
  private final PostRepository repo;
  private final EventBus bus;
  private final Post post;

  public CreatePostCommand(PostRepository repo, EventBus bus, Post post) {
    this.repo = repo;
    this.bus = bus;
    this.post = post;
  }

  @Override
  public void execute() {
    repo.save(post);
    bus.publish(Events.POSTS_REPLACED, null);
  }

  @Override
  public void undo() {
    repo.delete(post.id());
    bus.publish(Events.POSTS_REPLACED, null);
  }

  @Override
  public String getDescription() {
    return "Create post: " + post.title();
  }
}
