package org.example.delivery.review.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReviewCreateRequest {

  @NotNull(message = "reviewStar is required")
  private String reviewStar;

  @NotNull(message = "content is required")
  private String content;
}
