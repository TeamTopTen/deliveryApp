package org.example.delivery.review.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewRequest {

  @NotBlank(message = "내용을 입력해 주세요.")
  private String content;

  @NotNull(message = "Enum 값은 반드시 입력해야 합니다.")
  private  String star;

  public ReviewRequest() {
  }
}
