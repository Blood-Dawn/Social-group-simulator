package org.campusboard.sgs.repo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.campusboard.sgs.model.Role;
import org.campusboard.sgs.model.User;

public class InMemoryUserRepository implements UserRepository {
  private final Map<String, User> users = new HashMap<>();
  private final Map<String, String> passwordHashes = new HashMap<>();

  public InMemoryUserRepository() {
    add(new User("admin", "admin123", Role.ADMIN));
    add(new User("staff01", "staff123", Role.STAFF));
    add(new User("stud01", "stud123", Role.STUDENT));
  }

  @Override
  public Optional<User> find(String username) {
    return Optional.ofNullable(users.get(username));
  }

  @Override
  public void add(User u) {
    users.put(u.username(), u);
    passwordHashes.put(u.username(), hash(u.password()));
  }

  @Override
  public java.util.Collection<User> listAll() {
    return java.util.Collections.unmodifiableCollection(users.values());
  }

  @Override
  public boolean validatePassword(String username, String password) {
    String stored = passwordHashes.get(username);
    if (stored == null) {
      return false;
    }
    return stored.equals(hash(password));
  }

  @Override
  public void assignPassword(String username, String password) {
    if (users.containsKey(username)) {
      passwordHashes.put(username, hash(password));
    }
  }

  private String hash(String raw) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hashed = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(hashed);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("Missing SHA-256", e);
    }
  }
}
