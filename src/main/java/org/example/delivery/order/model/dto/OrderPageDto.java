package org.example.delivery.order.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.delivery.common.domain.entity.Order;
import org.example.delivery.common.domain.enums.OrderStatus;

@Getter
@AllArgsConstructor
public class OrderPageDto {

  private Long orderId;
  private String userName;
  private String storeName;
  private String menuName;
  private Integer menuPrice;
  private OrderStatus orderStatus;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public OrderPageDto(Order order) {
    this.orderId = order.getId();
    this.userName = order.getUser().getName();
    this.storeName = order.getStore().getName();
    this.menuName = order.getMenu().getName();
    this.menuPrice = order.getMenu().getPrice();
    this.orderStatus = order.getOrderStatus();
    this.createdAt = order.getCreatedAt();
    this.updatedAt = order.getUpdatedAt();
  }
}
