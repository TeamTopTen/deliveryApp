package org.example.delivery.menu.controller;


import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.delivery.auth.model.dto.AuthUser;
import org.example.delivery.menu.model.request.MenuRequest;
import org.example.delivery.menu.model.response.MenuResponse;
import org.example.delivery.menu.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.delivery.auth.Annotation.Auth;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("api/menus")
@Slf4j
@RequiredArgsConstructor
public class MenuController {

  private final MenuService menuService;

  @PostMapping("/owners")
  public ResponseEntity<String> createMenu(@Valid @RequestBody MenuRequest request,
      @Auth AuthUser authUser) {


    if(!authUser.email().equals(request.getEmail())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    menuService.createMenu(request, authUser.email());
    return ResponseEntity.ok().body("메뉴가 정상적으로 생성되었습니다.");
  }

  @GetMapping("/users/stores/{store_id}")
  public ResponseEntity<List<MenuResponse>> findMenu(@PathVariable Long storeId) {

    return ResponseEntity.ok().body(menuService.findMenu(storeId));
  }

  @PutMapping("{menu_id}/owners")
  public ResponseEntity<MenuResponse> putMenu(@PathVariable("menu_id") Long id,
      @Valid @RequestBody MenuRequest request,
      @Auth AuthUser authUser) {

    if(!authUser.email().equals(request.getEmail())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    return ResponseEntity.ok().body(menuService.putMenu(id,request,authUser.email()));
  }

  @PatchMapping("{menu_id}/owners")
  public ResponseEntity<String> softDeleteMenu(@PathVariable("menu_id") Long id,
      @Auth AuthUser authUser) {

    menuService.softDeleteMenu(id,authUser.email());

    return ResponseEntity.ok("메뉴가 정상적으로 삭제되었습니다.");
  }
}