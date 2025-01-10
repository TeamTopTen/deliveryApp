package org.example.delivery.review.service;

import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.delivery.auth.model.dto.AuthUser;
import org.example.delivery.common.domain.Order;
import org.example.delivery.common.domain.OrderStatus;
import org.example.delivery.common.domain.Review;
import org.example.delivery.common.domain.ReviewStar;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.AccessDeniedException;
import org.example.delivery.common.exception.base.ConflictException;
import org.example.delivery.common.exception.base.NotFoundException;
import org.example.delivery.order.repository.OrderRepository;
import org.example.delivery.review.model.dto.ReviewPageDto;
import org.example.delivery.review.model.request.ReviewCreateRequest;
import org.example.delivery.review.model.request.ReviewUpdateRequest;
import org.example.delivery.review.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final OrderRepository orderRepository;
  private final ReviewRepository reviewRepository;

  @Transactional
  public void createReview(
      AuthUser authUser,
      Long orderId,
      ReviewCreateRequest request) {
    Long userId = authUser.id();

    Order order = orderRepository.findByIdOrThrow(orderId);

    if (!userId.equals(order.getUser().getId())) {
      throw new AccessDeniedException(ErrorCode.USER_ACCESS_DENIED);
    }

    if (reviewRepository.existsByOrderId(orderId)) {
      throw new ConflictException(ErrorCode.REVIEW_ALREADY_EXISTS);
    }

    if(OrderStatus.COMPLETED != order.getOrderStatus()) {
      throw new AccessDeniedException(ErrorCode.REVIEW_NOT_ORDER_COMPLETED);
    }

    ReviewStar reviewStar = ReviewStar.of(request.getReviewStar());
    Review review = new Review(order, reviewStar, request.getContent());

    reviewRepository.save(review);
  }

  @Transactional(readOnly = true)
  public Page<ReviewPageDto> findReviews(AuthUser authUser, Long storeId, Pageable pageable) {

    return reviewRepository.findReviewsByStoreId(storeId, pageable);
  }

  @Transactional(readOnly = true)
  public Page<ReviewPageDto> getReviewsByStarRange(Long storeId, Integer minStar, Integer maxStar,
      Pageable pageable) {
    List<ReviewStar> stars = Arrays.stream(ReviewStar.values())
        .filter(star -> (star.getValue() >= minStar) && (star.getValue() <= maxStar))
        .toList();
    return reviewRepository.findReviewsByStarRange(storeId, stars, pageable);
  }

  @Transactional
  public void updateReview(AuthUser authUser, Long reviewId, ReviewUpdateRequest request) {
    Long userId = authUser.id();

    Review review = reviewRepository.findReviewByIdOrElseThrow(reviewId);

    Long orderId = review.getOrder().getId();

    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new AccessDeniedException(ErrorCode.ORDER_ACCESS_DENIED)
    );

    if (!order.getUser().getId().equals(userId)) {
      throw new AccessDeniedException(ErrorCode.USER_ACCESS_DENIED);
    }
    ReviewStar reviewStar = ReviewStar.of(request.getReviewStar());

    review.changeReview(reviewStar, request.getContent());
  }

  @Transactional
  public void deleteReview(AuthUser authUser, Long reviewId) {
    Long userId = authUser.id();

    Review review = reviewRepository.findReviewByIdOrElseThrow(reviewId);

    Long orderId = review.getOrder().getId();

    Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND)
    );

    if (!order.getUser().getId().equals(userId)) {
      throw new AccessDeniedException(ErrorCode.ORDER_ACCESS_DENIED);
    }

    reviewRepository.delete(review);
  }
}
