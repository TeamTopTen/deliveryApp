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
  @NotBlank
  @NotNull
  private String email;

  @NotBlank
  @NotNull
  private String name;


  @NotNull
  private Integer price;
  private Long storeId;

}
