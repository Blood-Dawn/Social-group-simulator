package org.campusboard.sgs.controller;

import java.util.*;

/**
 * Event bus for decoupled communication between components.
 * Uses AppEvent enum for type safety.
 */
public class EventBus {
    private Map<AppEvent, List<Runnable>> listeners = new HashMap<>();

    public void subscribe(AppEvent event, Runnable action) {
        listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(action);
    }

    public void publish(AppEvent event) {
        if (listeners.containsKey(event)) {
            listeners.get(event).forEach(Runnable::run);
        }
    }
    
    public void unsubscribe(AppEvent event) {
        listeners.remove(event);
    }
}
