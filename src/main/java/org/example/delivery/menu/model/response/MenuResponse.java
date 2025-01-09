package org.example.delivery.menu.model.response;

import static java.awt.SystemColor.menu;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.example.delivery.common.domain.Menu;

@Getter
public class MenuResponse {

  private String name;
  private Integer price;

  private MenuResponse(String name, Integer price) {
    this.name = name;
    this.price = price;
  }

  static public MenuResponse createMenuResponse(String name, Integer price) {
    return new MenuResponse(name,price);
  }

  static public List<MenuResponse> createMenuResponseList(List<Menu> menuList) {
    List<MenuResponse> MenuResponseList = new ArrayList<>();
    int i =0;
    for (  Menu menu:menuList ) {
      MenuResponseList.add(i,MenuResponse.createMenuResponse(menu.getName(),menu.getPrice()));
      i++;
    }
    return MenuResponseList;
  }
}
