package org.example.delivery.common.domain.enums;

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

  public static UserRole of(String userRole) {

    return Arrays.stream(UserRole.values()).filter(r -> r.name()
            .equalsIgnoreCase(userRole)).findFirst()
            .orElseThrow(() -> new InvalidRequestException(ErrorCode.INVALID_REQUEST));
  }
}
