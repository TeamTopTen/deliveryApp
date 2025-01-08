package org.example.delivery.order.model.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.delivery.common.domain.Menu;
import org.example.delivery.common.domain.Order;
import org.example.delivery.common.domain.Store;
import org.example.delivery.common.domain.User;

@Getter
@AllArgsConstructor
public class OrderDto {
  private Long orderId;
  private String userName;
  private String storeName;
  private String menuName;
  private float menuPrice;
  private String orderStatus;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public OrderDto(Order order) {
    this.orderId = order.getId();
    this.userName = order.getUser().getUserName();
    this.storeName = order.getStore();
    this.menuName = order.getMenu();
    this.menuPrice = order.getMenu();
    this.orderStatus = order.getOrderStatus();
    this.createdAt = order.getCreatedAt();
    this.updatedAt = order.getUpdatedAt();
  }
}
