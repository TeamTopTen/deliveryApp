package org.example.delivery.menu.controller;


import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.delivery.menu.model.request.MenuRequest;
import org.example.delivery.menu.model.response.MenuResponse;
import org.example.delivery.menu.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/menus")
@Slf4j
@RequiredArgsConstructor
public class MenuContoller {

  private MenuService menuService;

  @PostMapping("/owners")
  public ResponseEntity<String> createMenu(@Valid @RequestBody MenuRequest request,
      @RequestHeader("Authorization") String authorization) {

//    String token = authorization.substring(7);
//
//    claims claims = jwtTokenProvider.validateProvider(token);
//
//    String email = claims.getSubject();
//
//    request.getEmail() ==

        menuService.createMenu(request,email);
    return ResponseEntity.ok().body("메뉴가 정상적으로 생성되었습니다.");
  }

  @GetMapping("/users/stores/{store_id}")
  public ResponseEntity<List<MenuResponse>> createMenu(@PathVariable Long storeId,
      @RequestHeader("Authorization") String authorization) {

//    String token = authorization.substring(7);
//
//    claims claims = jwtTokenProvider.validateProvider(token);
//
//    String email = claims.getSubject();
//
//    request.getEmail() ==

    menuService.findMenu(storeId,email);

    return ResponseEntity.ok().body(menuService.findMenu(storeId,email));
  }

}