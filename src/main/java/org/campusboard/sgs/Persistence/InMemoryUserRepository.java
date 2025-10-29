package org.campusboard.sgs.persistence;

import org.campusboard.sgs.model.User;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.Base64;
import java.util.Arrays;

public class InMemoryUserRepository implements UserRepository {
    private final Map<UUID, User> users = new ConcurrentHashMap<>();
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public List<User> findAll() {
        return users.values().stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id))
                .filter(User::isActive);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }
        String target = username.toLowerCase(Locale.ROOT);
        return users.values().stream()
                .filter(User::isActive)
                .filter(user -> user.getUsername() != null && user.getUsername().equalsIgnoreCase(target))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null) {
            return Optional.empty();
        }
        String target = email.toLowerCase(Locale.ROOT);
        return users.values().stream()
                .filter(User::isActive)
                .filter(user -> user.getEmail() != null && user.getEmail().equalsIgnoreCase(target))
                .findFirst();
    }

    @Override
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (user == null || !users.containsKey(user.getId())) {
            return null;
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean delete(UUID id) {
        User user = users.get(id);
        if (user == null) {
            return false;
        }
        user.setActive(false);
        return true;
    }

    @Override
    public void assignPassword(User user, char[] password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'assignPassword'");
    }

    @Override
    public boolean validatePassword(User user, char[] password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validatePassword'");
    }
}
