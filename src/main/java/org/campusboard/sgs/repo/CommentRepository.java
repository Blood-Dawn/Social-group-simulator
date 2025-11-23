package org.campusboard.sgs.repo;

import java.util.List;
import java.util.UUID;
import org.campusboard.sgs.model.Comment;

public interface CommentRepository {
  List<Comment> findByPost(UUID postId);
  void add(Comment comment);
  void clear();
}
