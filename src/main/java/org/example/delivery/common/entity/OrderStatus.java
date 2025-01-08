package org.example.delivery.common.entity;

public enum OrderStatus {
  ORDERED,         // 주문 생성됨
  CONFIRMED,       // 가게에서 주문 확인
  PREPARING,       // 음식/상품 준비 중
  OUT_FOR_DELIVERY,// 배달 진행 중
  COMPLETED,       // 배달 완료
  CANCELLED,       // 주문 취소됨
  FAILED           // 주문 실패
}
