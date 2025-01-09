package org.example.delivery.order.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.delivery.auth.Annotation.Auth;
import org.example.delivery.auth.model.dto.AuthUser;
import org.example.delivery.order.model.dto.OrderDto;
import org.example.delivery.order.model.dto.OrderPageDto;
import org.example.delivery.order.model.request.OrderCreateRequest;
import org.example.delivery.order.model.request.OrderUpdateRequest;
import org.example.delivery.order.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

  private final OrderService orderService;

  @PostMapping("/orders/stores/{storeId}/menus/{menuId}")
  public ResponseEntity<String> createOrder(
      @Auth AuthUser authUser,
      @PathVariable("storeId") Long storeId,
      @PathVariable("menuId") Long menuId,
      @Valid @RequestBody OrderCreateRequest request
  ) {
    orderService.createOrder(authUser, storeId, menuId, request);
    return new ResponseEntity<>("등록되었습니다. ", HttpStatus.CREATED);
  }


  @GetMapping("/orders")
  public ResponseEntity<Page<OrderPageDto>> getOrders(
      @Auth AuthUser authUser,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "1") int page
  ) {
    Pageable pageable = PageRequest.of(page - 1, size);

    return new ResponseEntity<>(orderService.findOrders(authUser, pageable),
        HttpStatus.OK);
  }


  @GetMapping("/orders/{orderId}")
  public ResponseEntity<OrderDto> getOrders(
      @Auth AuthUser authUser,
      @PathVariable("orderId") Long orderId
  ) {
    return new ResponseEntity<>(orderService.findOrderbyOrderId(authUser, orderId),
        HttpStatus.OK);
  }

  @PatchMapping("/orders/{orderId}")
  public ResponseEntity<String> updateOrderStatus(
      @Auth AuthUser authUser,
      @PathVariable("orderId") Long orderId,
      @Valid @RequestBody OrderUpdateRequest request
  ) {
    orderService.updateOrderStatus(authUser, orderId, request);
    return new ResponseEntity<>("수정 되었습니다. ", HttpStatus.OK);
  }


  @PatchMapping("/orders/{orderId}/deletion")
  public ResponseEntity<String> updateOrderStatus(
      @Auth AuthUser authUser,
      @PathVariable("orderId") Long orderId
  ) {
    orderService.softDeleteOrder(authUser, orderId);
    return new ResponseEntity<>("삭제 되었습니다. ", HttpStatus.OK);
  }

}
