package org.campusboard.sgs.Persistence;

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
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        char[] copy = Arrays.copyOf(password, password.length);
        try {
            String salt = generateSalt();
            String hash = hashPassword(copy, salt);
            user.setPasswordSalt(salt);
            user.setPasswordHash(hash);
            users.put(user.getId(), user);
        } finally {
            Arrays.fill(copy, '\0');
        }
    }

    @Override
    public boolean validatePassword(User user, char[] password) {
        if (user == null || password == null) {
            return false;
        }
        String storedHash = user.getPasswordHash();
        String salt = user.getPasswordSalt();
        if (storedHash == null || salt == null) {
            return false;
        }
        String hash = hashPassword(password, salt);
        return MessageDigest.isEqual(storedHash.getBytes(StandardCharsets.UTF_8), hash.getBytes(StandardCharsets.UTF_8));
    }

    private String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String hashPassword(char[] password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(Base64.getDecoder().decode(salt));
            byte[] passwordBytes = new String(password).getBytes(StandardCharsets.UTF_8);
            byte[] hash = digest.digest(passwordBytes);
            Arrays.fill(passwordBytes, (byte) 0);
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }
}