package org.campusboard.sgs;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.Duration;
import java.util.List;
import javax.swing.*;
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
            boolean useRemoteRepository = shouldUseRemoteRepository(args);

            PostRepository postRepo;
            RemotePostSyncClient syncClient = null;

            if (useRemoteRepository) {
                String remoteUrl = resolveRemoteUrl();
                Duration pollInterval = resolvePollInterval();
                System.out.println("🌐 Main: Using remote post repository at " + remoteUrl + " (poll interval " + pollInterval.toSeconds() + "s)");

                RemotePostRepository remoteRepo = new RemotePostRepository(remoteUrl);
                remoteRepo.addRemoteUpdateListener(posts -> EventBus.publish(AppEvent.POSTS_CHANGED, posts));
                remoteRepo.refreshFromRemote();

                syncClient = new RemotePostSyncClient(remoteRepo, pollInterval);
                syncClient.start();

                postRepo = remoteRepo;
            } else {
                System.out.println("💾 Main: Using in-memory post repository");
                postRepo = new InMemoryPostRepository();
            }

            UserRepository userRepo = new InMemoryUserRepository();
            Controller controller = new Controller(postRepo, userRepo);

            if (useRemoteRepository) {
                controller.setCurrentUser(createDefaultAdmin(userRepo));
            } else {
                seedDemoData(controller, postRepo, userRepo);
            }

            MainWindow mainWindow = new MainWindow(controller);
            if (syncClient != null) {
                RemotePostSyncClient finalSyncClient = syncClient;
                mainWindow.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        finalSyncClient.close();
                    }
                });
            }
            mainWindow.setVisible(true);
        });
    }

    private static boolean shouldUseRemoteRepository(String[] args) {
        for (String arg : args) {
            if ("--remote".equalsIgnoreCase(arg) || "--use-remote".equalsIgnoreCase(arg)) {
                return true;
            }
            if ("--in-memory".equalsIgnoreCase(arg)) {
                return false;
            }
        }

        String systemProperty = System.getProperty("sgs.remote.enabled");
        if (systemProperty != null) {
            return Boolean.parseBoolean(systemProperty);
        }

        String env = System.getenv("SGS_USE_REMOTE");
        if (env != null) {
            return Boolean.parseBoolean(env);
        }

        return false;
    }

    private static String resolveRemoteUrl() {
        String systemProperty = System.getProperty("sgs.remote.url");
        if (systemProperty != null && !systemProperty.isBlank()) {
            return systemProperty;
        }
        String env = System.getenv("SGS_REMOTE_URL");
        if (env != null && !env.isBlank()) {
            return env;
        }
        return "http://localhost:8080";
    }

    private static Duration resolvePollInterval() {
        String property = System.getProperty("sgs.remote.pollSeconds");
        if (property == null || property.isBlank()) {
            property = System.getenv("SGS_REMOTE_POLL_SECONDS");
        }
        if (property != null && !property.isBlank()) {
            try {
                long seconds = Long.parseLong(property);
                if (seconds > 0) {
                    return Duration.ofSeconds(seconds);
                }
            } catch (NumberFormatException e) {
                System.err.println("⚠️ Main: Invalid poll interval '" + property + "', falling back to default");
            }
        }
        return Duration.ofSeconds(5);
    }

    private static void seedDemoData(Controller controller, PostRepository postRepo, UserRepository userRepo) {
        User admin = createDefaultAdmin(userRepo);
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

    private static User createDefaultAdmin(UserRepository userRepo) {
        User admin = new User("admin", "admin@fau.edu", "Campus Admin", UserType.STAFF);
        userRepo.save(admin);
        return admin;
    }

    private static Post createPost(String title, String body, Category category, String author) {
        Post post = new Post(title, body, category);
        post.setAuthor(author);
        return post;
    }
}
