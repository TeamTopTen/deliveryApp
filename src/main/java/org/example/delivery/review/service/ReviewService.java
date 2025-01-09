package org.example.delivery.review.service;

import lombok.RequiredArgsConstructor;
import org.example.delivery.auth.model.dto.AuthUser;
import org.example.delivery.common.domain.Order;
import org.example.delivery.common.domain.Review;
import org.example.delivery.common.domain.ReviewStar;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.BusinessException;
import org.example.delivery.order.repository.OrderRepository;
import org.example.delivery.review.model.request.ReviewCreateRequest;
import org.example.delivery.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final OrderRepository orderRepository;
  private final ReviewRepository reviewRepository;

  public void createReview(
      AuthUser authUser,
      Long orderId,
      ReviewCreateRequest request) {

    if (reviewRepository.existsByOrderId(orderId)) {
      throw new BusinessException(ErrorCode.ORDER_ACCESS_DENIED);
    }

    Long userId = authUser.id();

    Order order = orderRepository.findByIdOrThrow(orderId);

    if (!userId.equals(order.getUser().getId())) {
      throw new BusinessException(ErrorCode.ORDER_ACCESS_DENIED);
    }

    if (!order.getOrderStatus().toString().equals("COMPLETED")){
      throw new BusinessException(ErrorCode.ORDER_ACCESS_DENIED);
    }

    ReviewStar reviewStar = ReviewStar.of(request.getReviewStar());

    Review review = new Review(order, reviewStar, request.getContent());

    reviewRepository.save(review);
  }
}
