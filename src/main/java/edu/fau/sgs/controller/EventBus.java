package edu.fau.sgs.controller;

import java.util.*;

public class EventBus {
    private Map<String, List<Runnable>> listeners = new HashMap<>();

    public void subscribe(String event, Runnable action) {
        listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(action);
    }

    public void publish(String event) {
        if (listeners.containsKey(event)) {
            listeners.get(event).forEach(Runnable::run);
        }
    }
}
