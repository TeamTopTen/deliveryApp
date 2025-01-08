package org.example.delivery.user.model;

import lombok.Getter;

@Getter
public enum UserRole {
  ADMIN("admin"),
  USER_ROLE("user");

  private final String userRole;

  UserRole(String userRole) {
    this.userRole = userRole;
  }
}
