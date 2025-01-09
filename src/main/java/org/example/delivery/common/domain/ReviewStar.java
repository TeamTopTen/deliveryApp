package org.example.delivery.common.domain;

import java.util.Arrays;
import lombok.Getter;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.BusinessException;

@Getter
public enum ReviewStar {
  ONE("1"),
  TWO("2"),
  THREE("3"),
  FOUR("4"),
  FIVE("5");

  private final String value;  // Integer로 변경

  ReviewStar(String value) {
    this.value = value;
  }

  public static ReviewStar of(String value) {
    return Arrays.stream(ReviewStar.values())
        .filter(r -> r.value.equals(value))  // String 비교
        .findFirst()
        .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));
  }
}
