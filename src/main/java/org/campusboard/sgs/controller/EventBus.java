package org.campusboard.sgs.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * Event bus for decoupled communication between components.
 * Uses AppEvent enum for type safety.
 */
public class EventBus {
    private static final EnumMap<AppEvent, List<Consumer<Object>>> listeners = new EnumMap<>(AppEvent.class);

    public static void subscribe(AppEvent event, Consumer<Object> listener) {
        listeners.computeIfAbsent(event, key -> new ArrayList<>()).add(listener);
    }

    public static void publish(AppEvent event) {
        publish(event, null);
    }

    public static void publish(AppEvent event, Object payload) {
        var eventListeners = listeners.getOrDefault(event, Collections.emptyList());
        eventListeners.forEach(listener -> listener.accept(payload));
    }

    public static void unsubscribe(AppEvent event) {
        listeners.remove(event);
    }
}
