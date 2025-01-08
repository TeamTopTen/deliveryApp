package org.example.delivery.order.model.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.delivery.common.entity.Menu;
import org.example.delivery.common.entity.Order;
import org.example.delivery.common.entity.OrderStatus;
import org.example.delivery.common.entity.Store;
import org.example.delivery.common.entity.User;

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
