package org.example.delivery.review.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.delivery.common.domain.ReviewStar;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewPageDto {
  private Long reviewId;
  private String menuName;
  private Integer menuPrice;
  private ReviewStar reviewStar;
  private String content;
}
