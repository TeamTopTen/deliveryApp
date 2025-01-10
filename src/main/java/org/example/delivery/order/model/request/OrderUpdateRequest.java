package org.example.delivery.order.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderUpdateRequest {

  @NotNull(message = "주문 상태를 입력하세요.")
  private String orderStatus;

}
