package org.campusboard.sgs.controller;

import org.campusboard.sgs.Seeds;
import org.campusboard.sgs.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for EventBus event system
 */
class EventBusTest {

    @BeforeEach
    void setUp() {
        // Clear all subscriptions before each test
        for (AppEvent event : AppEvent.values()) {
            EventBus.unsubscribe(event);
        }
    }

    @Test
    void subscriberReceivesPayload() {
        AtomicReference<Post> box = new AtomicReference<>();
        EventBus.subscribe(AppEvent.POST_CREATED, e -> box.set((Post) e));

        Post p = Seeds.post("Hello");
        EventBus.publish(AppEvent.POST_CREATED, p);

        assertNotNull(box.get(), "Subscriber should receive payload");
        assertEquals("Hello", box.get().getTitle(), "Payload should be the published post");
    }

    @Test
    void publishWithoutPayload() {
        AtomicBoolean called = new AtomicBoolean(false);
        EventBus.subscribe(AppEvent.POSTS_CHANGED, e -> called.set(true));

        EventBus.publish(AppEvent.POSTS_CHANGED);

        assertTrue(called.get(), "Subscriber should be called even without payload");
    }

    @Test
    void multipleSubscribersReceiveEvent() {
        AtomicInteger count = new AtomicInteger(0);

        EventBus.subscribe(AppEvent.POSTS_CHANGED, e -> count.incrementAndGet());
        EventBus.subscribe(AppEvent.POSTS_CHANGED, e -> count.incrementAndGet());
        EventBus.subscribe(AppEvent.POSTS_CHANGED, e -> count.incrementAndGet());

        EventBus.publish(AppEvent.POSTS_CHANGED);

        assertEquals(3, count.get(), "All three subscribers should be called");
    }

    @Test
    void differentEventsDontInterfere() {
        AtomicBoolean postCreated = new AtomicBoolean(false);
        AtomicBoolean postDeleted = new AtomicBoolean(false);

        EventBus.subscribe(AppEvent.POST_CREATED, e -> postCreated.set(true));
        EventBus.subscribe(AppEvent.POST_DELETED, e -> postDeleted.set(true));

        EventBus.publish(AppEvent.POST_CREATED);

        assertTrue(postCreated.get(), "POST_CREATED subscriber should be called");
        assertFalse(postDeleted.get(), "POST_DELETED subscriber should not be called");
    }

    @Test
    void unsubscribeRemovesListeners() {
        AtomicBoolean called = new AtomicBoolean(false);
        EventBus.subscribe(AppEvent.POSTS_CHANGED, e -> called.set(true));

        EventBus.unsubscribe(AppEvent.POSTS_CHANGED);
        EventBus.publish(AppEvent.POSTS_CHANGED);

        assertFalse(called.get(), "Subscriber should not be called after unsubscribe");
    }

    @Test
    void publishingUnsubscribedEventDoesNothing() {
        // Should not throw exception
        assertDoesNotThrow(() -> EventBus.publish(AppEvent.ERROR_OCCURRED));
    }

    @Test
    void payloadCanBeNull() {
        AtomicBoolean called = new AtomicBoolean(false);
        AtomicReference<Object> receivedPayload = new AtomicReference<>();

        EventBus.subscribe(AppEvent.DATA_LOADED, e -> {
            called.set(true);
            receivedPayload.set(e);
        });

        EventBus.publish(AppEvent.DATA_LOADED, null);

        assertTrue(called.get(), "Subscriber should be called");
        assertNull(receivedPayload.get(), "Payload should be null");
    }

    @Test
    void subscriberCanModifyPayload() {
        Post p = Seeds.post("Original");
        AtomicReference<String> titleBox = new AtomicReference<>();

        EventBus.subscribe(AppEvent.POST_CREATED, e -> {
            Post post = (Post) e;
            titleBox.set(post.getTitle());
        });

        EventBus.publish(AppEvent.POST_CREATED, p);

        assertEquals("Original", titleBox.get(), "Subscriber should receive original title");
    }

    @Test
    void multipleEventsCanBePublishedSequentially() {
        AtomicInteger createCount = new AtomicInteger(0);
        AtomicInteger deleteCount = new AtomicInteger(0);

        EventBus.subscribe(AppEvent.POST_CREATED, e -> createCount.incrementAndGet());
        EventBus.subscribe(AppEvent.POST_DELETED, e -> deleteCount.incrementAndGet());

        EventBus.publish(AppEvent.POST_CREATED);
        EventBus.publish(AppEvent.POST_CREATED);
        EventBus.publish(AppEvent.POST_DELETED);

        assertEquals(2, createCount.get(), "Should receive 2 create events");
        assertEquals(1, deleteCount.get(), "Should receive 1 delete event");
    }
}
