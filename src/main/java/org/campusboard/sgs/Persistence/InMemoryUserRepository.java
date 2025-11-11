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
        if (password == null || password.length == 0) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        char[] passwordCopy = Arrays.copyOf(password, password.length);
        try {
            byte[] salt = new byte[32];
            RANDOM.nextBytes(salt);

            String saltEncoded = Base64.getEncoder().encodeToString(salt);
            String hash = hashPassword(passwordCopy, salt);

            user.setPasswordSalt(saltEncoded);
            user.setPasswordHash(hash);
            user.setFailedLoginAttempts(0);

            users.put(user.getId(), user);
        } finally {
            Arrays.fill(passwordCopy, '\0');
        }
    }

    @Override
    public boolean validatePassword(User user, char[] password) {
        if (user == null || password == null || password.length == 0) {
            return false;
        }

        String saltEncoded = user.getPasswordSalt();
        String expectedHash = user.getPasswordHash();
        if (saltEncoded == null || expectedHash == null) {
            return false;
        }

        byte[] salt;
        try {
            salt = Base64.getDecoder().decode(saltEncoded);
        } catch (IllegalArgumentException e) {
            return false;
        }

        String candidateHash = hashPassword(password, salt);
        return MessageDigest.isEqual(expectedHash.getBytes(StandardCharsets.UTF_8),
                candidateHash.getBytes(StandardCharsets.UTF_8));
    }

    private String hashPassword(char[] password, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] hashed = digest.digest(new String(password).getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
