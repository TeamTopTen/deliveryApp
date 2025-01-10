package org.example.delivery.review.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.delivery.common.domain.ReviewStar;

@AllArgsConstructor
@Getter
public class ReviewCreateRequest {

  @NotNull(message = "reviewStar is required")
  private Integer reviewStar;

  @NotNull(message = "content is required")
  private String content;
}
