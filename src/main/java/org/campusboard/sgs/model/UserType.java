package org.campusboard.sgs.model;

public enum UserType {
  GUEST,
  STUDENT,
  STAFF,
  ADMIN;

  public static UserType fromRole(Role role) {
    return switch (role) {
      case GUEST -> GUEST;
      case STUDENT -> STUDENT;
      case STAFF -> STAFF;
      case ADMIN -> ADMIN;
    };
  }

  public Role toRole() {
    return switch (this) {
      case GUEST -> Role.GUEST;
      case STUDENT -> Role.STUDENT;
      case STAFF -> Role.STAFF;
      case ADMIN -> Role.ADMIN;
    };
  }
}
