package org.campusboard.sgs;

import javax.swing.*;
import java.util.List;
import org.campusboard.sgs.controller.*;
import org.campusboard.sgs.Persistence.*;
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

            seedDemoData(controller, postRepo, userRepo);

            MainWindow mainWindow = new MainWindow(controller);
            mainWindow.setVisible(true);
        });
    }

    private static void seedDemoData(Controller controller, PostRepository postRepo, UserRepository userRepo) {
        User admin = new User("admin", "admin@fau.edu", "Campus Admin", UserType.STAFF);
        userRepo.save(admin);
        controller.setCurrentUser(admin);

        List<Post> demoPosts = List.of(
                createPost("Welcome to Campus Board!", "This is your go-to spot for announcements, events, and more.", Category.ANNOUNCEMENTS, "admin"),
                createPost("Study Group for COP3330", "Looking for teammates to prep for the midterm this weekend.", Category.STUDY_GROUPS, "john_doe"),
                createPost("Basketball Game Tonight!", "FAU vs FIU at 7pm in the arena. Free pizza for students!", Category.EVENTS, "sports_fan"),
                createPost("Lost Backpack", "Navy backpack left in the library yesterday. Please DM if found.", Category.LOST_AND_FOUND, "sarah_owls")
        );

        demoPosts.forEach(post -> {
            postRepo.save(post);
            EventBus.publish(AppEvent.POST_CREATED, post);
        });

        EventBus.publish(AppEvent.POSTS_CHANGED);
    }

    private static Post createPost(String title, String body, Category category, String author) {
        Post post = new Post(title, body, category);
        post.setAuthor(author);
        return post;
    }
}
