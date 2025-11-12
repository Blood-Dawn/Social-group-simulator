package org.campusboard.sgs.util;

public enum Events {
  POSTS_REPLACED, // wholesale changes
  POST_UPDATED, // payload = postId
  SEARCH_CHANGED,
  FILTER_CHANGED,
  USER_LOGGED_IN,
  USER_LOGGED_OUT,
  SHOW_LOGIN;

  public Payload of(Object data) {
    return new Payload(this, data);
  }

  public static final class Payload {
    public final Events type;
    public final Object data;

    public Payload(Events t, Object d) {
      this.type = t;
      this.data = d;
    }
  }
}
