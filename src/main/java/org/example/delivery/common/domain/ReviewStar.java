package org.example.delivery.common.domain;

import java.util.Arrays;
import lombok.Getter;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.BusinessException;
import org.example.delivery.common.exception.base.InvalidRequestException;

@Getter
public enum ReviewStar {
  ONE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5);

  private final int reviewStar;

  ReviewStar(int reviewStar) {
    this.reviewStar = reviewStar;
  }

  public static ReviewStar of(int reviewStar) {
    return Arrays.stream(ReviewStar.values())
        .filter(r -> r.reviewStar == reviewStar)
        .findFirst()
        .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));
  }
}
