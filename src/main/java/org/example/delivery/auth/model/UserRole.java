package org.example.delivery.auth.model;

import java.util.Arrays;
import lombok.Getter;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.InvalidRequestException;

@Getter
public enum UserRole {
  OWNER("owner"),
  USER("user");

  private final String userRole;

  UserRole(String userRole) {
    this.userRole = userRole;
  }

  //string을 받았을 때  enum으로 매핑ㄷ죄게..
  public static UserRole of(String userRole) {
    return Arrays.stream(UserRole.values()).filter(r -> r.name().equalsIgnoreCase(userRole))
        .findFirst().orElseThrow(() -> new InvalidRequestException(
            ErrorCode.INVALID_REQUEST));
  }
}
