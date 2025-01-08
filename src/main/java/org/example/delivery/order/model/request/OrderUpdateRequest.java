package org.example.delivery.order.model.request;

import jakarta.validation.constraints.NotNull;

public class OrderUpdateRequest {
  @NotNull(message = "Order is required")
  private String orderStatus;
}
