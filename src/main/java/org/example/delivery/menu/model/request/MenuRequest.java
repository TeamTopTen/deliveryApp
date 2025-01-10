package org.example.delivery.menu.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MenuRequest {

  public MenuRequest() {
  }
  @NotBlank(message = "빈칸으로 제출할 수 없습니다")
  private String email;

  @NotBlank(message = "빈칸으로 제출할 수 없습니다")
  private String name;

  @NotNull
  private Integer price;
}
