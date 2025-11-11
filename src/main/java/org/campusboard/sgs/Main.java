package org.campusboard.sgs;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import org.campusboard.sgs.Persistence.*;
import org.campusboard.sgs.controller.*;
import org.campusboard.sgs.view.*;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            boolean useRemoteRepository = shouldUseRemoteRepository(args);

            PostRepository postRepo;
            RemotePostSyncClient syncClient = null;

            if (useRemoteRepository) {
                String remoteUrl = resolveRemoteUrl();
                Duration pollInterval = resolvePollInterval();
                System.out.println("🌐 Main: Using remote post repository at " + remoteUrl + " (poll interval "
                        + pollInterval.toSeconds() + "s)");

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

            seedDemoData(controller, postRepo, userRepo);

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
            mainWindow.showLoginPrompt();
        });
    }

    private static void seedDemoData(Controller controller, PostRepository postRepo, UserRepository userRepo) {
        // Pre-seeded users ensure demo posts have persistent author references for the
        // UI.
        User admin = new User("admin", "admin@fau.edu", "Campus Admin", UserType.STAFF);
        User john = new User("john_doe", "john.doe@fau.edu", "John Doe", UserType.STUDENT);
        User athleticsClub = new User("sports_fan", "athletics@fau.edu", "Owls Athletics", UserType.CLUB);
        User sarah = new User("sarah_owls", "sarah.owls@fau.edu", "Sarah O.", UserType.STUDENT);

        userRepo.save(admin);
        userRepo.assignPassword(admin, "admin123".toCharArray());
        userRepo.save(john);
        userRepo.assignPassword(john, "student123".toCharArray());
        userRepo.save(athleticsClub);
        userRepo.assignPassword(athleticsClub, "club123".toCharArray());
        userRepo.save(sarah);
        userRepo.assignPassword(sarah, "student123".toCharArray());

        controller.setCurrentUser(admin);

        List<Post> demoPosts = List.of(
                createPost("Welcome to Campus Board!", "This is your go-to spot for announcements, events, and more.",
                        Category.ANNOUNCEMENTS, admin),
                createPost("Study Group for COP3330", "Looking for teammates to prep for the midterm this weekend.",
                        Category.STUDY_GROUPS, john),
                createPost("Basketball Game Tonight!", "FAU vs FIU at 7pm in the arena. Free pizza for students!",
                        Category.EVENTS, athleticsClub),
                createPost("Lost Backpack", "Navy backpack left in the library yesterday. Please DM if found.",
                        Category.LOST_AND_FOUND, sarah));

        demoPosts.forEach(post -> {
            postRepo.save(post);
            EventBus.publish(AppEvent.POST_CREATED, post);
        });

        EventBus.publish(AppEvent.POSTS_CHANGED);
    }

    private static Post createPost(String title, String body, Category category, User author) {
        return new Post(title, body, category, author);
    }

    private static boolean shouldUseRemoteRepository(String[] args) {
        if (args != null) {
            for (String arg : args) {
                if (arg == null) {
                    continue;
                }
                String trimmed = arg.trim().toLowerCase(Locale.ENGLISH);
                if ("--remote".equals(trimmed) || "--remote=true".equals(trimmed)) {
                    return true;
                }
                if ("--remote=false".equals(trimmed)) {
                    return false;
                }
            }
        }

        String envFlag = System.getenv("SGS_REMOTE_ENABLED");
        if (envFlag != null) {
            String normalized = envFlag.trim().toLowerCase(Locale.ENGLISH);
            if (!normalized.isEmpty()) {
                return normalized.equals("true")
                        || normalized.equals("1")
                        || normalized.equals("yes")
                        || normalized.equals("on");
            }
        }

        return Boolean.getBoolean("sgs.remote");
    }

    private static String resolveRemoteUrl() {
        String envUrl = System.getenv("SGS_REMOTE_URL");
        if (envUrl != null && !envUrl.isBlank()) {
            return envUrl.trim();
        }

        String sysProp = System.getProperty("sgs.remote.url");
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp.trim();
        }

        return "http://localhost:8080";
    }

    private static Duration resolvePollInterval() {
        String envInterval = System.getenv("SGS_REMOTE_POLL_INTERVAL_SECONDS");
        if (envInterval != null && !envInterval.isBlank()) {
            try {
                long seconds = Long.parseLong(envInterval.trim());
                if (seconds > 0) {
                    return Duration.ofSeconds(seconds);
                }
            } catch (NumberFormatException ignored) {
                System.err.println("⚠️ Main: Invalid SGS_REMOTE_POLL_INTERVAL_SECONDS value, falling back to default");
            }
        }

        String sysProp = System.getProperty("sgs.remote.poll.seconds");
        if (sysProp != null && !sysProp.isBlank()) {
            try {
                long seconds = Long.parseLong(sysProp.trim());
                if (seconds > 0) {
                    return Duration.ofSeconds(seconds);
                }
            } catch (NumberFormatException ignored) {
                System.err.println("⚠️ Main: Invalid sgs.remote.poll.seconds property, falling back to default");
            }
        }

        return Duration.ofSeconds(5);
    }
}
