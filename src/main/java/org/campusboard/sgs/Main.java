package org.campusboard.sgs;

import javax.swing.*;
import org.campusboard.sgs.controller.*;
import org.campusboard.sgs.demo.DemoDataSeeder;
import org.campusboard.sgs.model.*;
import org.campusboard.sgs.repo.*;
import org.campusboard.sgs.util.*;
import org.campusboard.sgs.view.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::start);
    }

    private static void start() {
        var bus = new EventBus();
        var session = new Session();
        var users = new InMemoryUserRepository();
        var posts = new InMemoryPostRepository();
        var comments = new InMemoryCommentRepository();

        seedUsers(users);
        long seed = DemoDataSeeder.resolveSeed();
        DemoDataSeeder.ensureDemoData(posts, users, seed);
        DemoDataSeeder.ensureDemoComments(comments, posts, seed ^ 0xBEEFCAFEL);

        var authCtl = new AuthController(users, session, bus);
        var commentCtl = new CommentController(comments, session, bus);
        var postCtl = new PostController(posts, users, session, bus);

        var mainWindow = new MainWindow(postCtl, commentCtl, authCtl, users, session, bus);
        mainWindow.setVisible(true);
    }

    private static void seedUsers(InMemoryUserRepository users) {
        users.add(new User("guest", "guest123", Role.GUEST));
        users.add(new User("student", "student123", Role.STUDENT));
        users.add(new User("staff", "staff123", Role.STAFF));
        users.add(new User("admin", "admin123", Role.ADMIN));
    }

}
