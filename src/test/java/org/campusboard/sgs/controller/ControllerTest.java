package org.campusboard.sgs.controller;

import org.campusboard.sgs.Seeds;
import org.campusboard.sgs.filter.CategoryFilter;
import org.campusboard.sgs.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Controller business logic
 */
class ControllerTest {

    private Controller controller;

    @BeforeEach
    void setUp() {
        controller = Seeds.controllerWithDemoData();
    }

    @Test
    void createPublishesEventAndPersists() {
        AtomicBoolean seen = new AtomicBoolean(false);
        EventBus.subscribe(AppEvent.POST_CREATED, e -> seen.set(true));

        int sizeBefore = controller.getAllPosts().size();
        controller.createPost("Lost Blue Backpack", "Please contact", Category.LOST_AND_FOUND);

        assertTrue(seen.get(), "POST_CREATED event should be published");
        assertEquals(sizeBefore + 1, controller.getAllPosts().size(), "Post should be persisted");
    }

    @Test
    void setFilterThenGetFiltered() {
        controller.setFilter(new CategoryFilter(Category.EVENTS));
        assertTrue(controller.getFiltered().stream()
                .allMatch(p -> p.getCategory() == Category.EVENTS),
                "All filtered posts should be EVENTS");
    }

    @Test
    void searchFiltersPostsByTitleAndBody() {
        controller.performSearch("basketball");
        var results = controller.getAllPosts();

        assertTrue(results.stream()
                .anyMatch(p -> p.getTitle().toLowerCase().contains("basketball") ||
                              p.getBody().toLowerCase().contains("basketball")),
                "Search results should contain 'basketball'");
    }

    @Test
    void undoRedoCreate() {
        int size = controller.getAllPosts().size();

        controller.createPostWithUndo("Test", "Body", Category.ANNOUNCEMENTS);
        assertEquals(size + 1, controller.getAllPosts().size(), "Post should be created");

        controller.undo();
        assertEquals(size, controller.getAllPosts().size(), "Post should be removed after undo");

        controller.redo();
        assertEquals(size + 1, controller.getAllPosts().size(), "Post should be restored after redo");
    }

    @Test
    void undoRedoDelete() {
        var posts = controller.getAllPosts();
        assertFalse(posts.isEmpty(), "Should have posts to test with");

        var postToDelete = posts.get(0);
        int size = posts.size();

        controller.deletePostWithUndo(postToDelete.getId());
        assertEquals(size - 1, controller.getAllPosts().size(), "Post should be deleted");

        controller.undo();
        assertEquals(size, controller.getAllPosts().size(), "Post should be restored after undo");

        controller.redo();
        assertEquals(size - 1, controller.getAllPosts().size(), "Post should be deleted again after redo");
    }

    @Test
    void undoRedoLike() {
        var posts = controller.getAllPosts();
        assertFalse(posts.isEmpty(), "Should have posts to test with");

        var postToLike = posts.get(0);
        int likesBefore = postToLike.getLikes();

        controller.likePostWithUndo(postToLike.getId());
        assertEquals(likesBefore + 1, controller.getAllPosts().stream()
                .filter(p -> p.getId().equals(postToLike.getId()))
                .findFirst().get().getLikes(), "Likes should increment");

        controller.undo();
        assertEquals(likesBefore, controller.getAllPosts().stream()
                .filter(p -> p.getId().equals(postToLike.getId()))
                .findFirst().get().getLikes(), "Likes should be restored after undo");
    }

    @Test
    void canUndoCanRedo() {
        assertFalse(controller.canUndo(), "Should not be able to undo initially");
        assertFalse(controller.canRedo(), "Should not be able to redo initially");

        controller.createPostWithUndo("Test", "Body", Category.GENERAL);
        assertTrue(controller.canUndo(), "Should be able to undo after command");
        assertFalse(controller.canRedo(), "Should not be able to redo yet");

        controller.undo();
        assertFalse(controller.canUndo(), "Should not be able to undo after single undo");
        assertTrue(controller.canRedo(), "Should be able to redo after undo");
    }

    @Test
    void createPostValidatesTitle() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.createPost("", "Body", Category.GENERAL);
        }, "Should throw for empty title");

        assertThrows(IllegalArgumentException.class, () -> {
            controller.createPost(null, "Body", Category.GENERAL);
        }, "Should throw for null title");
    }

    @Test
    void createPostValidatesBody() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.createPost("Title", "", Category.GENERAL);
        }, "Should throw for empty body");

        assertThrows(IllegalArgumentException.class, () -> {
            controller.createPost("Title", null, Category.GENERAL);
        }, "Should throw for null body");
    }

    @Test
    void clearFiltersResetsState() {
        controller.setFilter(new CategoryFilter(Category.EVENTS));
        controller.performSearch("test");

        controller.clearFilters();

        // After clearing, getAllPosts should return all posts (not filtered)
        int allPostsCount = controller.getAllPosts().size();
        assertTrue(allPostsCount > 0, "Should have posts after clearing filters");
    }

    @Test
    void eventBusPublishesPostsChanged() {
        AtomicBoolean postsChanged = new AtomicBoolean(false);
        EventBus.subscribe(AppEvent.POSTS_CHANGED, e -> postsChanged.set(true));

        controller.createPost("New Post", "Body", Category.GENERAL);
        assertTrue(postsChanged.get(), "POSTS_CHANGED should be published");
    }

    @Test
    void getAllPostsSortsByCreatedAtDescending() {
        var posts = controller.getAllPosts();
        if (posts.size() > 1) {
            for (int i = 0; i < posts.size() - 1; i++) {
                assertTrue(posts.get(i).getCreatedAt().isAfter(posts.get(i + 1).getCreatedAt()) ||
                          posts.get(i).getCreatedAt().isEqual(posts.get(i + 1).getCreatedAt()),
                          "Posts should be sorted by creation time descending");
            }
        }
    }
}
