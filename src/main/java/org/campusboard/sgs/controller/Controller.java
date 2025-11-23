package org.campusboard.sgs.controller;

import java.util.List;
import java.util.UUID;
import org.campusboard.sgs.filter.AllFilter;
import org.campusboard.sgs.filter.CategoryFilter;
import org.campusboard.sgs.filter.FilterStrategy;
import org.campusboard.sgs.model.Category;
import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.model.Role;
import org.campusboard.sgs.model.User;
import org.campusboard.sgs.model.UserType;
import org.campusboard.sgs.repo.PostRepository;
import org.campusboard.sgs.repo.UserRepository;
import org.campusboard.sgs.util.EventBus;
import org.campusboard.sgs.util.Session;

/**
 * Thin facade that bridges older call sites to the current Post/Auth controllers.
 * Keeps the merged Filters-Sort API surface compiling while deferring to the
 * maintained controllers.
 */
public class Controller {
  private final PostController postController;
  private final AuthController authController;
  private final Session session;

  public Controller(PostRepository postRepository, UserRepository userRepository, Session session, EventBus bus) {
    this.session = session;
    this.postController = new PostController(postRepository, userRepository, session, bus);
    this.authController = new AuthController(userRepository, session, bus);
  }

  public void createPost(String title, String body) {
    createPost(title, body, Category.ANNOUNCEMENTS);
  }

  public void createPost(String title, String body, Category category) {
    postController.create(title, body, category);
  }

  public void deletePost(UUID postId) {
    postController.findById(postId).ifPresent(postController::delete);
  }

  public void likePost(UUID postId) {
    postController.findById(postId).ifPresent(postController::toggleLike);
  }

  public List<Post> getAllPosts() {
    return postController.current();
  }

  public List<Post> getPostsByCategory(Category category) {
    if (category == null) {
      return getAllPosts();
    }
    postController.setFilter(new CategoryFilter(category));
    return postController.current();
  }

  public void applyFilter(Category category) {
    postController.setFilter(category == null ? new AllFilter() : new CategoryFilter(category));
  }

  public void clearFilters() {
    postController.setFilter(new AllFilter());
    postController.setSearch("");
  }

  public void performSearch(String query) {
    postController.setSearch(query);
  }

  public void setSort(FilterStrategy sortStrategy) {
    postController.setSort(sortStrategy);
  }

  public User getCurrentUser() {
    return authController.getCurrentUser();
  }

  public UserType getCurrentUserType() {
    Role role = authController.getCurrentRole();
    return role == null ? UserType.GUEST : UserType.fromRole(role);
  }

  public boolean login(String user, String pass) {
    return authController.login(user, pass);
  }

  public void logout() {
    authController.logout();
  }
}
