package org.campusboard.sgs.model;

import java.util.Objects;

/** Simple user record for auth/role gating. */
public final class User {
  private final String username;
  private final String password;
  private final Role role;

  public User(String username, String password, Role role) {
    this.username = Objects.requireNonNull(username);
    this.password = Objects.requireNonNull(password);
    this.role = Objects.requireNonNull(role);
  }
  public String username() { return username; }
  public String password() { return password; }
  public Role role() { return role; }
  public UserType getUserType() { return UserType.fromRole(role); }
}
