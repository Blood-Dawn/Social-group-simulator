package org.campusboard.sgs;

import org.campusboard.sgs.Persistence.InMemoryPostRepository;
import org.campusboard.sgs.Persistence.InMemoryUserRepository;
import org.campusboard.sgs.Persistence.PostRepository;
import org.campusboard.sgs.Persistence.UserRepository;
import org.campusboard.sgs.controller.Controller;
import org.campusboard.sgs.model.Category;
import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.model.User;
import org.campusboard.sgs.model.UserType;

import java.util.List;

/**
 * Seeds helper for creating test data
 */
public class Seeds {

    /**
     * Create a simple post with just a title
     */
    public static Post post(String title) {
        return new Post(title, "Body for " + title, Category.GENERAL, defaultUser());
    }

    /**
     * Create a post with title, body, and category
     */
    public static Post post(String title, String body, Category category) {
        return new Post(title, body, category, defaultUser());
    }

    /**
     * Create a post with likes for testing trending filters
     */
    public static Post postWithScore(int score) {
        Post post = new Post("Post with score " + score, "Test post", Category.GENERAL, defaultUser());
        if (score > 0) {
            post.setLikes(score);
        } else {
            post.setDislikes(Math.abs(score));
        }
        return post;
    }

    /**
     * Create a default test user
     */
    public static User defaultUser() {
        return new User("testuser", "test@test.com", "Test User", UserType.STUDENT);
    }

    /**
     * Create a user with specific type
     */
    public static User user(String username, UserType type) {
        return new User(username, username + "@test.com", username, type);
    }

    /**
     * Create a user with all fields
     */
    public static User user(String username, String email, String displayName, UserType type) {
        return new User(username, email, displayName, type);
    }

    /**
     * Create a repository with demo posts
     */
    public static PostRepository repoWithDemo() {
        InMemoryPostRepository repo = new InMemoryPostRepository();

        // Create test users
        User admin = user("admin", "admin@fau.edu", "Campus Admin", UserType.STAFF);
        User john = user("john_doe", "john.doe@fau.edu", "John Doe", UserType.STUDENT);
        User club = user("athletics", "athletics@fau.edu", "Owls Athletics", UserType.CLUB);
        User sarah = user("sarah_owls", "sarah.owls@fau.edu", "Sarah O.", UserType.STUDENT);

        // Create demo posts
        List<Post> demoPosts = List.of(
                new Post("Welcome to Campus Board!", "Official announcement", Category.ANNOUNCEMENTS, admin),
                new Post("Study Group for COP3330", "Looking for teammates", Category.STUDY_GROUPS, john),
                new Post("Basketball Game Tonight!", "FAU vs FIU at 7pm", Category.EVENTS, club),
                new Post("Lost Backpack", "Navy backpack in library", Category.LOST_AND_FOUND, sarah),
                new Post("Pizza Night", "Free pizza at student union", Category.EVENTS, club),
                new Post("Need Calc Help", "Can anyone tutor?", Category.ACADEMICS, john),
                new Post("Bike for Sale", "$50 OBO", Category.BUY_SELL, sarah),
                new Post("Club Fair Tomorrow", "Join student organizations", Category.CLUBS_ORGS, admin),
                new Post("Finals Week Tips", "Study strategies", Category.ACADEMICS, admin),
                new Post("Found Keys", "Blue keychain near gym", Category.LOST_AND_FOUND, john),
                new Post("Intramural Soccer", "Sign up by Friday", Category.SPORTS_FITNESS, club),
                new Post("Housing Available", "Roommate needed for summer", Category.HOUSING, sarah)
        );

        demoPosts.forEach(repo::save);
        return repo;
    }

    /**
     * Create a controller wired with the given repository
     */
    public static Controller controllerWith(PostRepository postRepo) {
        UserRepository userRepo = new InMemoryUserRepository();

        // Seed users
        User admin = user("admin", "admin@fau.edu", "Campus Admin", UserType.STAFF);
        User john = user("john_doe", "john.doe@fau.edu", "John Doe", UserType.STUDENT);
        User club = user("athletics", "athletics@fau.edu", "Owls Athletics", UserType.CLUB);
        User sarah = user("sarah_owls", "sarah.owls@fau.edu", "Sarah O.", UserType.STUDENT);

        userRepo.save(admin);
        userRepo.save(john);
        userRepo.save(club);
        userRepo.save(sarah);

        return new Controller(postRepo, userRepo);
    }

    /**
     * Create a controller with demo data already loaded
     */
    public static Controller controllerWithDemoData() {
        return controllerWith(repoWithDemo());
    }
}
