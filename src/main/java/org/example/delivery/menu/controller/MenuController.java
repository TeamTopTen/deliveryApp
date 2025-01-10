package org.example.delivery.menu.controller;


import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.delivery.auth.model.dto.AuthUser;
import org.example.delivery.menu.model.request.MenuRequest;
import org.example.delivery.menu.model.response.MenuResponse;
import org.example.delivery.menu.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.delivery.auth.annotation.Auth;

@RestController
@RequestMapping("api/stores/{store_id}/menus")
@Slf4j
@RequiredArgsConstructor
public class MenuController {

  private final MenuService menuService;

  @PostMapping("/owners")
  public ResponseEntity<String> createMenu(@Valid @RequestBody MenuRequest request,
      @PathVariable("store_id") Long storeId,
      @Auth AuthUser authUser
  ) {

    menuService.createMenu(request, authUser.email(),storeId);
    return ResponseEntity.ok().body("메뉴가 정상적으로 생성되었습니다.");
  }

  @GetMapping("/users")
  public ResponseEntity<List<MenuResponse>> findMenu(@PathVariable("store_id") Long storeId) {
    return ResponseEntity.ok().body(menuService.findMenu(storeId));
  }

  @PutMapping("{menu_id}/owners")
  public ResponseEntity<MenuResponse> putMenu(@PathVariable("menu_id") Long menuId,
      @PathVariable("store_id") Long storeId, //
      @Valid @RequestBody MenuRequest request,
      @Auth AuthUser authUser) {

    return ResponseEntity.ok().body(menuService.putMenu(menuId,request,authUser.email()));
  }

  @PatchMapping("{menu_id}/owners")
  public ResponseEntity<String> softDeleteMenu(@PathVariable("menu_id") Long menuId,
      @PathVariable("store_id") Long storeId, //
      @Auth AuthUser authUser) {

    menuService.softDeleteMenu(menuId,authUser.email());

    return ResponseEntity.ok("메뉴가 정상적으로 삭제되었습니다.");
  }
}