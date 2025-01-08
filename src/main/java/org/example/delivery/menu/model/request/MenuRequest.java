package org.example.delivery.menu.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@NotBlank
@NotNull
public class MenuRequest {

  public MenuRequest() {
  }

  private String email;
  private String name;
  private Integer price;
  private Long storeId;

}
