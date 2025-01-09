package org.example.delivery.menu.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.delivery.common.domain.Menu;

@AllArgsConstructor
@Getter
public class MenuDto {

  private String email;
  private String name;
  private Integer price;
  private Long storeId;
  private Long userId;
  private Boolean isDeleted;

  protected MenuDto() {
  }
}
