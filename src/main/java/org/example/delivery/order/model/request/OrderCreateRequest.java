package org.example.delivery.order.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderCreateRequest {

  @NotNull(message = "Order is required")
  private String orderStatus;

}
