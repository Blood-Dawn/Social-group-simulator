package org.campusboard.sgs.filter;

import java.util.List;
import java.util.stream.Stream;
import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.model.UserType;
import org.campusboard.sgs.repo.UserRepository;

public class AuthorTypeFilter implements FilterStrategy {
  private final UserType userType;
  private final UserRepository userRepo;

  public AuthorTypeFilter(UserType userType, UserRepository userRepo) {
    this.userType = userType;
    this.userRepo = userRepo;
  }

  @Override
  public Stream<Post> filter(List<Post> posts) {
    return posts.stream().filter(p -> userRepo.find(p.author())
        .map(user -> user.getUserType() == userType)
        .orElse(false));
  }

  @Override
  public String getDescription() {
    return userType.name() + " Posts";
  }

  public UserType getUserType() {
    return userType;
  }
}
