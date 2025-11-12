package org.campusboard.sgs;

import javax.swing.*;
import org.campusboard.sgs.controller.*;
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

        seedUsers(users);
        seedPosts(posts);

        var authCtl = new AuthController(users, session, bus);
        var postCtl = new PostController(posts, users, session, bus);

        var mainWindow = new MainWindow(postCtl, authCtl, users, session, bus);
        mainWindow.setVisible(true);
    }

    private static void seedUsers(InMemoryUserRepository users) {
        users.add(new User("guest", "guest123", Role.GUEST));
        users.add(new User("student", "student123", Role.STUDENT));
        users.add(new User("staff", "staff123", Role.STAFF));
        users.add(new User("admin", "admin123", Role.ADMIN));
    }

    private static void seedPosts(InMemoryPostRepository posts) {
        // 12 demo posts across different categories and authors
        posts.save(new Post(null,
                "Welcome to CampusBoard!",
                "This is your campus social hub. Share announcements, organize study groups, " +
                        "find lost items, and stay connected with your community.",
                Category.ANNOUNCEMENTS,
                "admin"));

        posts.save(new Post(null,
                "Campus WiFi Maintenance Scheduled",
                "The campus WiFi will undergo maintenance this Saturday from 2 AM to 6 AM. " +
                        "Please plan accordingly. Sorry for any inconvenience.",
                Category.ANNOUNCEMENTS,
                "staff"));

        posts.save(new Post(null,
                "Data Structures Study Group",
                "Looking for students interested in forming a study group for CS201 (Data Structures). " +
                        "We'll meet twice a week in the library. DM me if interested!",
                Category.STUDY_GROUPS,
                "student"));

        posts.save(new Post(null,
                "Calculus II Study Session",
                "Join us for a Calculus II review session this Thursday at 7 PM in Math Building Room 305. " +
                        "We'll cover integration techniques and applications. Free pizza!",
                Category.STUDY_GROUPS,
                "student"));

        posts.save(new Post(null,
                "Physics Lab Partners Needed",
                "Need 2-3 people for Physics 102 lab group. Monday/Wednesday mornings. " +
                        "Must be organized and ready to work!",
                Category.STUDY_GROUPS,
                "student"));

        posts.save(new Post(null,
                "Tech Conference This Weekend",
                "Don't miss the annual Tech Innovation Conference this Saturday! " +
                        "Featuring speakers from top tech companies, hands-on workshops, and networking opportunities. "
                        +
                        "Register at the student center.",
                Category.EVENTS,
                "staff"));

        posts.save(new Post(null,
                "Basketball Game Friday Night",
                "Come support our Owls! Home game this Friday at 7 PM. " +
                        "Student tickets are free with ID. Let's pack the stadium!",
                Category.EVENTS,
                "admin"));

        posts.save(new Post(null,
                "Career Fair Next Week",
                "The Spring Career Fair is next Tuesday from 10 AM to 4 PM in the Student Union. " +
                        "Over 100 employers will be there. Dress professionally and bring resumes!",
                Category.EVENTS,
                "staff"));

        posts.save(new Post(null,
                "Lost: Blue Backpack",
                "Lost my blue JanSport backpack near the Engineering building yesterday. " +
                        "Contains laptop and textbooks. Please contact me if found! Reward offered.",
                Category.LOST_FOUND,
                "student"));

        posts.save(new Post(null,
                "Found: Calculator in Library",
                "Found a TI-84 calculator on the 3rd floor of the library. " +
                        "Come to the circulation desk to claim it. Describe it to verify ownership.",
                Category.LOST_FOUND,
                "guest"));

        posts.save(new Post(null,
                "Lost: Car Keys",
                "Lost my car keys somewhere on campus today. Honda keychain with FAU tag. " +
                        "Please contact me ASAP if you find them!",
                Category.LOST_FOUND,
                "student"));

        posts.save(new Post(null,
                "Coding Competition Next Month",
                "Registration now open for the ACM Coding Competition! " +
                        "Form teams of 3-4 and compete for prizes. All skill levels welcome. " +
                        "Visit acm.fau.edu for details.",
                Category.EVENTS,
                "staff"));
    }
}
