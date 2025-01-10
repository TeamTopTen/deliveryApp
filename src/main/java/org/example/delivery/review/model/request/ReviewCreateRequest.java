package org.example.delivery.review.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReviewCreateRequest {

  @NotNull(message = "별점을 입력해주세요.")
  private Integer reviewStar;

  @NotNull(message = "내용을 입력해주세요.")
  private String content;
}
