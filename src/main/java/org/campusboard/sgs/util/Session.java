package org.campusboard.sgs.util;

import org.campusboard.sgs.model.*;

public class Session {
  private User user; // null = guest
  public boolean isAuthenticated(){ return user != null; }
  public String userIdOrGuest(){ return isAuthenticated() ? user.username() : "guest"; }
  public User user(){ return user; }
  public Role role(){ return isAuthenticated() ? user.role() : Role.GUEST; }
  public void setUser(User u){ this.user = u; }
  public void clear(){ this.user = null; }
}
