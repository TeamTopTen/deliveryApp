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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "review")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Review extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private ReviewStar reviewStar;

  @Column(nullable = false)
  private String content;

  public Review(Order order, ReviewStar reviewStar, String content){
    this.order = order;
    this.reviewStar = reviewStar;
    this.content = content;
  }

  public void changeReview(ReviewStar reviewStar, String content) {
    this.reviewStar = reviewStar;
    this.content = content;
  }

}

