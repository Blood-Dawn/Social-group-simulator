package org.campusboard.sgs.demo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.campusboard.sgs.model.Comment;
import org.campusboard.sgs.model.Category;
import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.repo.CommentRepository;
import org.campusboard.sgs.repo.PostRepository;
import org.campusboard.sgs.repo.UserRepository;

/**
 * Generates a robust set of demo posts to make the board feel populated and
 * realistic. Seeding only occurs when the repository is empty to avoid
 * overwriting user data.
 */
public final class DemoDataSeeder {
  private static final List<String> AUTHORS = List.of(
      "alex", "jordan", "maya", "riley", "sam", "taylor",
      "ashley", "kevin", "lee",
      "staff_miller", "staff_lee",
      "admin", "club_media", "club_cs", "guest");
  private static final List<String> LIKE_HANDLES = List.of(
      "stud01", "stud02", "stud03", "stud04", "stud05",
      "staff01", "staff02", "alumni01", "guest", "admin");

  private DemoDataSeeder() {}

  public static void ensureDemoData(PostRepository posts, UserRepository users) {
    ensureDemoData(posts, users, resolveSeed());
  }

  public static void ensureDemoData(PostRepository posts, UserRepository users, long seed) {
    if (posts.findAll().size() > 0) {
      return;
    }
    Random rng = new Random(seed);
    List<Post> seedPosts = new ArrayList<>();
    seedPosts.addAll(announcementPosts(rng));
    seedPosts.addAll(studyGroupPosts(rng));
    seedPosts.addAll(eventPosts(rng));
    seedPosts.addAll(lostFoundPosts(rng));

    Collections.shuffle(seedPosts, rng);

    seedPosts.forEach(posts::save);
  }

  public static void ensureDemoComments(CommentRepository comments, PostRepository posts) {
    ensureDemoComments(comments, posts, resolveSeed() ^ 0x9E3779B97F4A7C15L);
  }

  public static void ensureDemoComments(CommentRepository comments, PostRepository posts, long seed) {
    List<Post> all = posts.findAll();
    if (all.isEmpty() || hasAnyComments(comments, all)) {
      return;
    }
    Random commentRng = new Random(seed);
    int guaranteed = Math.min(12, all.size());
    for (int i = 0; i < all.size(); i++) {
      Post p = all.get(i);
      int count = (i < guaranteed) ? 1 + commentRng.nextInt(2) : commentRng.nextInt(2); // at least 1 for first batch
      for (int c = 0; c < count; c++) {
        String author = pickAuthor(commentRng);
        String body = sampleCommentBody(p.category(), c);
        comments.add(new Comment(null, p.id(), author, body, LocalDateTime.now().minusMinutes(commentRng.nextInt(240))));
      }
    }
  }

  private static List<Post> announcementPosts(Random rng) {
    List<PostTemplate> templates = List.of(
        new PostTemplate("Welcome Week Highlights",
            "Catch the recap of Welcome Week events and photos. Thanks to everyone who participated!",
            5, 22),
        new PostTemplate("Library Quiet Hours",
            "Reminder: Quiet hours after 9 PM in the main library. Group rooms are still available.",
            3, 12),
        new PostTemplate("Career Services Drop-Ins",
            "Resume reviews Tuesday/Thursday 2-4 PM. First-come, first-served.",
            2, 9),
        new PostTemplate("[TEST] Very long announcement with line breaks",
            "This is a stress test announcement.\n\nParagraph two with more text to ensure wrapping works.\n- Bullet style text\n- Another bullet\n\nEnd of message.",
            0, 3),
        new PostTemplate("Housing Maintenance Window",
            "Elevator inspections in Dorm A this Friday 8-11 AM. Expect brief delays.",
            1, 7),
        new PostTemplate("Shuttle Route Update",
            "Blue line adds a stop at Engineering. See posted schedules for details.",
            0, 5),
        new PostTemplate("Counseling Services",
            "Free drop-in counseling sessions all week. Location: Student Wellness Center.",
            1, 14),
        new PostTemplate("Dining Hall Menu Poll",
            "Vote on next month's rotating menu items! Link in portal.",
            0, 8),
        new PostTemplate("Campus WiFi Maintenance",
            "Overnight maintenance tonight 1-3 AM. Expect brief outages.",
            2, 11),
        new PostTemplate("[TEST] Edge case: zero likes",
            "This post intentionally starts with zero likes to test rendering.",
            0, 0),
        new PostTemplate("Security Alert Drill",
            "A campus-wide drill will run tomorrow at noon. No action needed beyond acknowledging alerts.",
            4, 10),
        new PostTemplate("Volunteer Tutors Needed",
            "Math and CS tutors needed for evening sessions. Training provided.",
            1, 9));
    return buildPosts(Category.ANNOUNCEMENTS, templates, rng);
  }

  private static List<Post> studyGroupPosts(Random rng) {
    List<PostTemplate> templates = List.of(
        new PostTemplate("Calculus II Study Session",
            "Weekly meetups Tue/Thu 7 PM, Math Building room 305. Bring practice problems.",
            2, 12),
        new PostTemplate("Physics 102 Lab Partners",
            "Looking for 2 partners for morning lab. Organized and ready to collaborate.",
            1, 6),
        new PostTemplate("Data Structures Review",
            "Sunday cram session in the library. We will cover trees/graphs. DM to join.",
            3, 18),
        new PostTemplate("Spanish Conversation Circle",
            "Beginner-friendly practice group. Cafeteria patio, Wednesdays at noon.",
            0, 7),
        new PostTemplate("Study Group for COP3330",
            "Meet Fri 18:00, library 2F. Focusing on design patterns this week.",
            1, 10),
        new PostTemplate("[TEST] Long body with many lines",
            "Line 1\nLine 2\nLine 3\nLine 4\nLine 5\nThis should wrap and keep spacing consistent.",
            0, 4),
        new PostTemplate("Organic Chem Review",
            "Reaction mechanisms practice Sunday 3 PM. Bring flashcards.",
            1, 5),
        new PostTemplate("Algorithms Practice",
            "Working through past contest problems. Meet in CS lounge 6 PM.",
            2, 14),
        new PostTemplate("Discrete Math Proofs",
            "Short proofs workshop Sat 11 AM. Snacks provided.",
            0, 6),
        new PostTemplate("History 201 Discussion",
            "Looking for partners to prep presentation on primary sources.",
            0, 5),
        new PostTemplate("Statistics Crash Course",
            "Reviewing hypothesis testing and confidence intervals. Monday 5 PM.",
            1, 8),
        new PostTemplate("Programming Languages Reading",
            "Reading group for FP/OO chapters. Thursday noon, café tables.",
            0, 4));
    return buildPosts(Category.STUDY_GROUPS, templates, rng);
  }

  private static List<Post> eventPosts(Random rng) {
    List<PostTemplate> templates = List.of(
        new PostTemplate("Tech Innovation Conference",
            "Speakers from industry, hands-on workshops, networking. Register at student center.",
            4, 25),
        new PostTemplate("Basketball Game Friday",
            "Home game at 7 PM. Student tickets free with ID. Let's pack the stadium!",
            6, 30),
        new PostTemplate("Career Fair Next Week",
            "Over 100 employers. Dress professionally and bring resumes. Student Union 10-4.",
            3, 15),
        new PostTemplate("Robotics Club Demo Night",
            "See autonomous bots in action. Friday 6 PM, Engineering Atrium.",
            2, 12),
        new PostTemplate("Outdoor Movie Night",
            "Bring a blanket. Showing starts at 8:30 PM on the quad.",
            1, 10),
        new PostTemplate("Art Walk",
            "Student art showcase across campus buildings. Map posted in portal.",
            0, 8),
        new PostTemplate("[TEST] Event description with paragraphs",
            "Paragraph one about the event.\n\nParagraph two with more details and timing.\n\nEnd.",
            0, 5),
        new PostTemplate("Hackathon Signups",
            "24-hour hackathon next month. Teams of up to 4. Prizes for best campus solutions.",
            3, 18),
        new PostTemplate("Open Mic Night",
            "Perform music, poetry, or comedy. Slots limited—sign up early.",
            1, 9),
        new PostTemplate("Wellness Yoga",
            "Sunrise yoga on the lawn, Saturday 7 AM. Beginners welcome.",
            0, 6),
        new PostTemplate("Board Game Night",
            "Casual play in the student union café, Wednesday 6 PM.",
            0, 7),
        new PostTemplate("Film Club Screening",
            "Classic film screening and discussion, Thursday 8 PM, Room 210.",
            0, 6));
    return buildPosts(Category.EVENTS, templates, rng);
  }

  private static List<Post> lostFoundPosts(Random rng) {
    List<PostTemplate> templates = List.of(
        new PostTemplate("Lost: Car Keys",
            "Honda keychain with FAU tag. Lost near CS building. Please contact if found.",
            0, 4),
        new PostTemplate("Found: TI-84 Calculator",
            "Found on library 3rd floor. Describe to claim.",
            0, 3),
        new PostTemplate("Lost Backpack",
            "Blue Jansport with laptop and textbooks. Reward offered.",
            1, 6),
        new PostTemplate("Found Student ID",
            "ID for Riley M. Turned in to front desk of Library.",
            0, 2),
        new PostTemplate("Lost Water Bottle",
            "Grey Hydro Flask with stickers. Last seen in Gym.",
            0, 2),
        new PostTemplate("Lost Headphones",
            "Black over-ear headphones left in study room B12.",
            0, 3),
        new PostTemplate("[TEST] Lost item – emoji test",
            "Lost backpack 🎒 with electronics 📱. Please reach out if seen.",
            0, 1),
        new PostTemplate("Found Notebook",
            "Spiral notebook with math notes. Pick up at Student Center desk.",
            0, 3),
        new PostTemplate("Lost Wallet",
            "Brown leather wallet. Cancelling cards tomorrow—please contact ASAP.",
            1, 5),
        new PostTemplate("Found Glasses",
            "Tortoise-shell frames found near cafeteria patio.",
            0, 3),
        new PostTemplate("Lost Keys on Lanyard",
            "Blue lanyard with dorm keys. Might be near parking lot C.",
            0, 4),
        new PostTemplate("Found Umbrella",
            "Striped umbrella left in lecture hall 120.",
            0, 2));
    return buildPosts(Category.LOST_FOUND, templates, rng);
  }

  private static List<Post> buildPosts(Category category, List<PostTemplate> templates, Random rng) {
    List<Post> results = new ArrayList<>();
    for (int i = 0; i < templates.size(); i++) {
      PostTemplate t = templates.get(i);
      String author = pickAuthor(rng);
      String title = applyAdminRule(t.title, author);
      LocalDateTime createdAt = LocalDateTime.now().minusMinutes(5 + rng.nextInt(60 * 24 * 10));
      Set<String> likedBy = sampleLikes(t.minLikes, t.maxLikes, rng);
      results.add(new Post(UUID.randomUUID(), title, t.body, category, author, createdAt, likedBy));
    }
    return results;
  }

  private static String pickAuthor(Random rng) {
    return AUTHORS.get(rng.nextInt(AUTHORS.size()));
  }

  private static Set<String> sampleLikes(int min, int max, Random rng) {
    if (max <= 0) {
      return Set.of();
    }
    int target = min + rng.nextInt(Math.max(1, max - min + 1));
    List<String> shuffled = new ArrayList<>(LIKE_HANDLES);
    Collections.shuffle(shuffled, rng);
    return shuffled.stream().limit(target).collect(Collectors.toSet());
  }

  private record PostTemplate(String title, String body, int minLikes, int maxLikes) {}

  private static boolean hasAnyComments(CommentRepository repo, List<Post> posts) {
    for (Post p : posts) {
      if (!repo.findByPost(p.id()).isEmpty()) {
        return true;
      }
    }
    return false;
  }

  private static String sampleCommentBody(Category category, int idx) {
    return switch (category) {
      case ANNOUNCEMENTS -> (idx % 2 == 0)
          ? "Thanks for the heads-up!"
          : "Following this update closely.";
      case STUDY_GROUPS -> (idx % 2 == 0)
          ? "I'd like to join, what materials should we prep?"
          : "Can we meet earlier? I have a lab later.";
      case EVENTS -> (idx % 2 == 0)
          ? "Looking forward to this."
          : "Is there a registration fee?";
      case LOST_FOUND -> (idx % 2 == 0)
          ? "I might have seen this near the library."
          : "Check the front desk; they keep lost items.";
    };
  }

  private static String applyAdminRule(String title, String author) {
    if ("admin".equalsIgnoreCase(author) && !title.startsWith("[TEST]")) {
      return "[TEST] " + title;
    }
    if (author != null && author.toLowerCase().startsWith("staff") && title.startsWith("[TEST]")) {
      return title.replaceFirst("^\\[TEST\\]\\s*", "");
    }
    return title;
  }

  public static long resolveSeed() {
    String prop = System.getProperty("sgs.demo.seed");
    if (prop != null && !prop.isBlank()) {
      try {
        return Long.parseLong(prop.trim());
      } catch (NumberFormatException ignored) {
      }
    }
    return new Random().nextLong();
  }
}
