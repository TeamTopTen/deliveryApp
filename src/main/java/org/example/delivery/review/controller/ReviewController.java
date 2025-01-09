package org.example.delivery.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.delivery.auth.Annotation.Auth;
import org.example.delivery.auth.model.dto.AuthUser;
import org.example.delivery.auth.model.response.RegisterResponse;
import org.example.delivery.review.model.request.ReviewRequest;
import org.example.delivery.review.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;

  @PostMapping("/stores/{store_id}/orders/{order_id}/reviews")
  public ResponseEntity<String> reviewCreatAPI(@Valid @RequestBody ReviewRequest request,
      @PathVariable("store_id") Long storeId,
      @PathVariable("order_id") Long orderId,
      @Auth AuthUser authUser) {

    reviewService.reviewCreate(request,storeId,orderId);

    return ResponseEntity.ok().body("주문에 성공하였습니다.");

  }
}
