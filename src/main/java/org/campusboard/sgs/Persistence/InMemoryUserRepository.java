package org.campusboard.sgs.Persistence;

import org.campusboard.sgs.model.User;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepository implements UserRepository {
    private final Map<UUID, User> users = new ConcurrentHashMap<>();

    @Override
    public List<User> findAll() {
        // TODO: Return list of all active users
        return null;
    }

    @Override
    public Optional<User> findById(UUID id) {
        // TODO: Find user by UUID
        return null;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        // TODO: Find user by username (case-insensitive)
        return null;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        // TODO: Find user by email (case-insensitive)
        return null;
    }

    @Override
    public User save(User user) {
        // TODO: Save new user to map
        return null;
    }

    @Override
    public User update(User user) {
        // TODO: Update existing user if exists
        return null;
    }

    @Override
    public boolean delete(UUID id) {
        // TODO: Set user as inactive rather than deleting
        return false;
    }
}