package org.example.delivery.order.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.delivery.order.model.dto.OrderDto;
import org.example.delivery.order.model.request.OrderCreateRequest;
import org.example.delivery.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
  private final OrderService orderService;

  @PostMapping("/orders/stores/{storeId}/menus/{menuId}")
  public ResponseEntity<String> createOrder(
                                            @PathVariable("storeId") Long storeId,
                                            @PathVariable("menuId") Long menuId,
                                            @Valid @RequestBody OrderCreateRequest request
                                              ){
    orderService.createOrder(storeId, menuId, request);
    return new ResponseEntity<>("등록되었습니다. ", HttpStatus.CREATED);
  };

}
