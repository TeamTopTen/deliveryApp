package org.example.delivery.review.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReviewUpdateRequest {

  @NotNull(message = "reviewStar is required")
  private Integer reviewStar;

  @NotNull(message = "content is required")
  private String content;

}
