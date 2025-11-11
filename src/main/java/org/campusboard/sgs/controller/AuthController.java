package org.campusboard.sgs.controller;

import org.campusboard.sgs.model.*;
import org.campusboard.sgs.repo.*;
import org.campusboard.sgs.util.*;

public class AuthController {
  private final UserRepository users;
  private final Session session;
  private final EventBus bus;

  public AuthController(UserRepository users, Session session, EventBus bus){
    this.users = users; this.session = session; this.bus = bus;
  }

  public boolean login(String user, String pass){
    var u = users.find(user).orElse(null);
    if (u!=null && u.password().equals(pass)){
      session.setUser(u);
      bus.publish(Events.USER_LOGGED_IN, u.username());
      return true;
    }
    return false;
  }

  public void logout(){
    session.clear();
    bus.publish(Events.USER_LOGGED_OUT);
  }
}
