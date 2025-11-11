package org.campusboard.sgs.filter;

import org.campusboard.sgs.model.*;
import org.campusboard.sgs.repo.UserRepository;
import java.util.List;
import java.util.stream.Stream;

public class AuthorTypeFilter implements FilterStrategy {
  private final UserType userType;
  private final UserRepository userRepo;

  public AuthorTypeFilter(UserType userType, UserRepository userRepo) {
    this.userType = userType;
    this.userRepo = userRepo;
  }

  @Override
  public Stream<Post> filter(List<Post> posts) {
    return posts.stream().filter(p -> {
      var user = userRepo.find(p.author());
      return user.isPresent() && user.get().getUserType() == userType;
    });
  }

  @Override
  public String getDescription() {
    return userType.name() + " Posts";
  }

  public UserType getUserType() {
    return userType;
  }
}
