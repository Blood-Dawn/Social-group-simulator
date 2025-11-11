package org.campusboard.sgs.filter;

import org.campusboard.sgs.Seeds;
import org.campusboard.sgs.model.Category;
import org.campusboard.sgs.model.Post;
import org.campusboard.sgs.model.UserType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for filter strategies
 */
class FiltersTest {

    @Test
    void allFilterReturnsAllPosts() {
        List<Post> posts = List.of(
                Seeds.post("Post 1"),
                Seeds.post("Post 2"),
                Seeds.post("Post 3")
        );

        AllFilter filter = new AllFilter();
        List<Post> result = filter.apply(posts);

        assertEquals(3, result.size(), "AllFilter should return all posts");
    }

    @Test
    void categoryFilterKeepsOnlyMatchingCategory() {
        List<Post> posts = List.of(
                Seeds.post("Event 1", "Body", Category.EVENTS),
                Seeds.post("Announcement", "Body", Category.ANNOUNCEMENTS),
                Seeds.post("Event 2", "Body", Category.EVENTS),
                Seeds.post("General", "Body", Category.GENERAL)
        );

        CategoryFilter filter = new CategoryFilter(Category.EVENTS);
        List<Post> result = filter.apply(posts);

        assertEquals(2, result.size(), "Should only keep EVENTS posts");
        assertTrue(result.stream().allMatch(p -> p.getCategory() == Category.EVENTS),
                "All results should be EVENTS category");
    }

    @Test
    void trendingFilterKeepsPostsAboveThreshold() {
        List<Post> posts = List.of(
                Seeds.postWithScore(5),
                Seeds.postWithScore(10),
                Seeds.postWithScore(1),
                Seeds.postWithScore(8)
        );

        TrendingFilter filter = new TrendingFilter(6);
        List<Post> result = filter.apply(posts);

        assertEquals(2, result.size(), "Should keep posts with score >= 6");
        assertTrue(result.stream().allMatch(p -> (p.getLikes() - p.getDislikes()) >= 6),
                "All results should have score >= 6");
    }

    @Test
    void trendingFilterWithZeroThreshold() {
        List<Post> posts = List.of(
                Seeds.postWithScore(5),
                Seeds.postWithScore(-3),
                Seeds.postWithScore(0)
        );

        TrendingFilter filter = new TrendingFilter(0);
        List<Post> result = filter.apply(posts);

        assertEquals(2, result.size(), "Should keep posts with score >= 0");
    }

    @Test
    void authorTypeFilterKeepsOnlyMatchingType() {
        List<Post> posts = List.of(
                Seeds.post("Post by student", "Body", Category.GENERAL),
                new Post("Post by staff", "Body", Category.ANNOUNCEMENTS, Seeds.user("staff", UserType.STAFF)),
                new Post("Post by club", "Body", Category.EVENTS, Seeds.user("club", UserType.CLUB)),
                new Post("Another staff post", "Body", Category.GENERAL, Seeds.user("admin", UserType.STAFF))
        );

        AuthorTypeFilter filter = new AuthorTypeFilter(UserType.STAFF);
        List<Post> result = filter.apply(posts);

        assertEquals(2, result.size(), "Should keep only STAFF posts");
        assertTrue(result.stream().allMatch(p -> p.getAuthor().getUserType() == UserType.STAFF),
                "All results should be from STAFF users");
    }

    @Test
    void authorTypeFilterHandlesNullAuthors() {
        List<Post> posts = List.of(
                Seeds.post("Post 1", "Body", Category.GENERAL),
                new Post("Staff Post", "Body", Category.ANNOUNCEMENTS, Seeds.user("staff", UserType.STAFF))
        );

        AuthorTypeFilter filter = new AuthorTypeFilter(UserType.STUDENT);
        List<Post> result = filter.apply(posts);

        // Should only include the student post (from Seeds.post which uses defaultUser which is STUDENT)
        assertEquals(1, result.size());
    }

    @Test
    void emptyListReturnsEmptyResult() {
        List<Post> posts = List.of();

        assertEquals(0, new AllFilter().apply(posts).size());
        assertEquals(0, new CategoryFilter(Category.EVENTS).apply(posts).size());
        assertEquals(0, new TrendingFilter(5).apply(posts).size());
        assertEquals(0, new AuthorTypeFilter(UserType.STAFF).apply(posts).size());
    }
}
