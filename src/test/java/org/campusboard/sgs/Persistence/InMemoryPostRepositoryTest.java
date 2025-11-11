package org.campusboard.sgs.Persistence;

import org.campusboard.sgs.Seeds;
import org.campusboard.sgs.model.Category;
import org.campusboard.sgs.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for InMemoryPostRepository
 */
class InMemoryPostRepositoryTest {

    private InMemoryPostRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPostRepository();
    }

    @Test
    void saveThenFindById() {
        Post post = Seeds.post("Test Post");
        repository.save(post);

        Optional<Post> found = repository.findById(post.getId());
        assertTrue(found.isPresent());
        assertEquals("Test Post", found.get().getTitle());
    }

    @Test
    void findByIdReturnsEmptyWhenNotFound() {
        Optional<Post> found = repository.findById(UUID.randomUUID());
        assertFalse(found.isPresent());
    }

    @Test
    void findAllReturnsAllPosts() {
        Post post1 = Seeds.post("Post 1");
        Post post2 = Seeds.post("Post 2");
        Post post3 = Seeds.post("Post 3");

        repository.save(post1);
        repository.save(post2);
        repository.save(post3);

        List<Post> all = repository.findAll();
        assertEquals(3, all.size());
    }

    @Test
    void deleteRemovesPost() {
        Post post = Seeds.post("Delete Me");
        repository.save(post);

        assertTrue(repository.findById(post.getId()).isPresent());

        boolean deleted = repository.delete(post.getId());
        assertTrue(deleted);
        assertFalse(repository.findById(post.getId()).isPresent());
    }

    @Test
    void deleteReturnsFalseWhenPostNotFound() {
        boolean deleted = repository.delete(UUID.randomUUID());
        assertFalse(deleted);
    }

    @Test
    void updateModifiesExistingPost() {
        Post post = Seeds.post("Original Title");
        repository.save(post);

        post.setTitle("Updated Title");
        repository.update(post);

        Optional<Post> found = repository.findById(post.getId());
        assertTrue(found.isPresent());
        assertEquals("Updated Title", found.get().getTitle());
    }

    @Test
    void likePostIncrementsLikes() {
        Post post = Seeds.post("Like This");
        repository.save(post);

        assertEquals(0, post.getLikes());

        Post liked = repository.likePost(post.getId());
        assertNotNull(liked);
        assertEquals(1, liked.getLikes());
    }

    @Test
    void dislikePostIncrementsDislikes() {
        Post post = Seeds.post("Dislike This");
        repository.save(post);

        assertEquals(0, post.getDislikes());

        Post disliked = repository.dislikePost(post.getId());
        assertNotNull(disliked);
        assertEquals(1, disliked.getDislikes());
    }

    @Test
    void saveRequiresAuthor() {
        assertThrows(NullPointerException.class, () -> {
            Post post = new Post("Title", "Body", Category.GENERAL, null);
            repository.save(post);
        });
    }

    @Test
    void replaceAllClearsAndAddsNewPosts() {
        Post post1 = Seeds.post("Post 1");
        repository.save(post1);

        List<Post> newPosts = List.of(
                Seeds.post("New Post 1"),
                Seeds.post("New Post 2")
        );

        repository.replaceAll(newPosts);

        List<Post> all = repository.findAll();
        assertEquals(2, all.size());
        assertFalse(repository.findById(post1.getId()).isPresent());
    }
}
