package org.campusboard.sgs;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import org.campusboard.sgs.Persistence.*;
import org.campusboard.sgs.controller.*;
import org.campusboard.sgs.model.*;
import org.campusboard.sgs.view.MainWindow;

/**
 * Main entry point for the Campus Board application.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PostRepository postRepo = new InMemoryPostRepository();
            UserRepository userRepo = new InMemoryUserRepository();
            Controller controller = new Controller(postRepo, userRepo);

            seedDemoData(postRepo, userRepo);

            MainWindow mainWindow = new MainWindow(controller);
            mainWindow.setVisible(true);
            mainWindow.showLoginPrompt();
        });
    }

    private static void seedDemoData(PostRepository postRepo, UserRepository userRepo) {
        createDemoUser(userRepo, new User("admin", "admin@fau.edu", "Campus Admin", UserType.STAFF), "admin123");
        createDemoUser(userRepo, new User("staff", "staff@fau.edu", "FAU Staff", UserType.STAFF), "staff123");
        createDemoUser(userRepo, new User("student", "student@fau.edu", "FAU Student", UserType.STUDENT), "student123");
        createDemoUser(userRepo, new User("guest", "guest@fau.edu", "Campus Guest", UserType.GUEST), "guest123");

        List<Post> demoPosts = List.of(
                createPost("Welcome to Campus Board!", "This is your go-to spot for announcements, events, and more.", Category.ANNOUNCEMENTS, "admin"),
                createPost("Study Group for COP3330", "Looking for teammates to prep for the midterm this weekend.", Category.STUDY_GROUPS, "student"),
                createPost("Basketball Game Tonight!", "FAU vs FIU at 7pm in the arena. Free pizza for students!", Category.EVENTS, "staff"),
                createPost("Visitor Parking Update", "Guest parking is free after 6pm near the stadium.", Category.CAMPUS_SERVICES, "guest")
        );

        demoPosts.forEach(post -> {
            postRepo.save(post);
            EventBus.publish(AppEvent.POST_CREATED, post);
        });

        EventBus.publish(AppEvent.POSTS_CHANGED);
    }

    private static void createDemoUser(UserRepository userRepo, User user, String password) {
        userRepo.save(user);
        char[] passwordChars = password.toCharArray();
        userRepo.assignPassword(user, passwordChars);
        Arrays.fill(passwordChars, '\0');
    }

    private static Post createPost(String title, String body, Category category, String author) {
        Post post = new Post(title, body, category);
        post.setAuthor(author);
        return post;
    }
}
