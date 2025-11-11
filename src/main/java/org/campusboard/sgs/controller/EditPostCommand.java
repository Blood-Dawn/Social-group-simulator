package org.campusboard.sgs.controller;

import org.campusboard.sgs.model.*;
import org.campusboard.sgs.repo.PostRepository;
import org.campusboard.sgs.util.*;

public class EditPostCommand implements Command {
  private final PostRepository repo;
  private final EventBus bus;
  private final Post post;
  private final String oldTitle;
  private final String oldBody;
  private final Category oldCategory;
  private final String newTitle;
  private final String newBody;
  private final Category newCategory;

  public EditPostCommand(PostRepository repo, EventBus bus, Post post,
                         String newTitle, String newBody, Category newCategory) {
    this.repo = repo;
    this.bus = bus;
    this.post = post;
    this.oldTitle = post.title();
    this.oldBody = post.body();
    this.oldCategory = post.category();
    this.newTitle = newTitle;
    this.newBody = newBody;
    this.newCategory = newCategory;
  }

  @Override
  public void execute() {
    post.setTitle(newTitle);
    post.setBody(newBody);
    post.setCategory(newCategory);
    repo.update(post);
    bus.publish(Events.POST_UPDATED, post.id());
  }

  @Override
  public void undo() {
    post.setTitle(oldTitle);
    post.setBody(oldBody);
    post.setCategory(oldCategory);
    repo.update(post);
    bus.publish(Events.POST_UPDATED, post.id());
  }

  @Override
  public String getDescription() {
    return "Edit post: " + post.title();
  }
}
