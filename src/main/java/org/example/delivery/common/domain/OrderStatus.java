package org.example.delivery.common.domain;

import java.util.Arrays;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.BusinessException;
import org.example.delivery.common.exception.base.InvalidRequestException;

public enum OrderStatus {
  ORDERED,         // 주문 생성됨
  CONFIRMED,       // 가게에서 주문 확인
  PREPARING,       // 음식/상품 준비 중
  OUT_FOR_DELIVERY,// 배달 진행 중
  COMPLETED,       // 배달 완료
  CANCELLED,       // 주문 취소됨
  FAILED;           // 주문 실패

  public static OrderStatus of(String role) {
    return Arrays.stream(OrderStatus.values())
        .filter(r -> r.name().equalsIgnoreCase(role))
        .findFirst()
        .orElseThrow(() -> new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
  }
}
