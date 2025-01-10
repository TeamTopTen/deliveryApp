package org.example.delivery.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.delivery.auth.Annotation.Auth;
import org.example.delivery.auth.model.dto.AuthUser;
import org.example.delivery.review.model.dto.ReviewPageDto;
import org.example.delivery.review.model.request.ReviewCreateRequest;
import org.example.delivery.review.model.request.ReviewUpdateRequest;
import org.example.delivery.review.service.ReviewService;
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
public class ReviewController {

  private final ReviewService reviewService;

  // 리뷰 생성 Post /api/orders/{order_id}/reviews
  @PostMapping("/orders/{orderId}/reviews")
  public ResponseEntity<String> createReview(
      @Auth AuthUser authUser,
      @PathVariable("orderId") Long orderId,
      @Valid @RequestBody ReviewCreateRequest request
  ) {

    reviewService.createReview(authUser, orderId, request);

    return new ResponseEntity<>("등록되었습니다. ", HttpStatus.CREATED);
  }

  // 리뷰 전체 최신순 조회 GET /api/stores/{store_id}/reviews
  @GetMapping("/stores/{storeId}/reviews")
  public ResponseEntity<Page<ReviewPageDto>> getReviews(
      @Auth AuthUser authUser,
      @PathVariable("storeId") Long storeId,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "1") int page
  ) {
    Pageable pageable = PageRequest.of(page - 1, size);

    return new ResponseEntity<>(reviewService.findReviews(authUser, storeId, pageable),
        HttpStatus.OK);
  }

  // 리뷰 별점 범위 조회 GET /api/stores/{store_id}/reviews?minStar={minStar}&maxStar={maxStar}
  @GetMapping("/stores/{storeId}/reviews-star")
  public ResponseEntity<Page<ReviewPageDto>> getReviewsByStarRange(
      @PathVariable("storeId") Long storeId,
      @RequestParam("minStar") Integer minStar,
      @RequestParam("maxStar") Integer maxStar,
      Pageable pageable
  ) {

    return new ResponseEntity<>(reviewService.getReviewsByStarRange(storeId, minStar, maxStar,
        pageable), HttpStatus.OK);
  }

  //리뷰 수정 /api/orders/{order_id}/reviews/{review_id}
  @PatchMapping("/reviews/{reviewId}")
  public ResponseEntity<String> updateReview(
      @Auth AuthUser authUser,
      @PathVariable("reviewId") Long reviewId,
      @Valid @RequestBody ReviewUpdateRequest request
  ) {
    reviewService.updateReview(authUser, reviewId, request);
    return new ResponseEntity<>("리뷰수정이 완료되었습니다.", HttpStatus.OK);
  }

  // 리뷰 삭제 /api/orders/{order_id}/reviews/{review_id}
}
