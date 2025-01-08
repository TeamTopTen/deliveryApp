package org.example.delivery.common.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "`order`")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Order extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

//  @ManyToOne
//  @JoinColumn(name = "store_id", nullable = false)
//  private Store store;
//
//  @ManyToOne
//  @JoinColumn(name = "menu_id", nullable = false)
//  private Menu menu;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;


  public Order(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }


  public void changeOrderStatus(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
  }
}

