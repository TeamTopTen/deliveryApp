package org.example.delivery.common.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "review")
@Getter
public class Review extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "store_id")
  private Store store;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  @Column(length = 100)
  private String content;

  @Enumerated(EnumType.STRING)
  private String star;

  protected Review() {
  }

  private Review(User user, Store store, Order order, String content, String star) {
    this.user = user;
    this.store = store;
    this.order = order;
    this.content = content;
    this.star = star;
  }

  public static Review createReview(User user, Store store, Order order, String content, String star) {
    return new Review(user,store,order,content,star);
  }
}

