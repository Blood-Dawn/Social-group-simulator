package org.campusboard.sgs.repo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.campusboard.sgs.model.Comment;

public class InMemoryCommentRepository implements CommentRepository {
  private final Map<UUID, List<Comment>> commentsByPost = new ConcurrentHashMap<>();

  @Override
  public List<Comment> findByPost(UUID postId) {
    return new ArrayList<>(commentsByPost.getOrDefault(postId, List.of()));
  }

  @Override
  public void add(Comment comment) {
    commentsByPost.computeIfAbsent(comment.postId(), k -> new ArrayList<>()).add(comment);
  }

  @Override
  public void clear() {
    commentsByPost.clear();
  }
}
