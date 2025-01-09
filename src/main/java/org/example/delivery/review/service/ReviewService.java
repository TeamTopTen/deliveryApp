package org.example.delivery.review.service;

import lombok.RequiredArgsConstructor;
import org.example.delivery.auth.repository.UserRepository;
import org.example.delivery.common.domain.Order;
import org.example.delivery.common.domain.Review;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.AccessDeniedException;
import org.example.delivery.common.exception.base.InvalidRequestException;
import org.example.delivery.common.exception.base.NotFoundException;
import org.example.delivery.order.repository.OrderRepository;
import org.example.delivery.review.model.request.ReviewRequest;
import org.example.delivery.review.repository.ReviewRepository;
import org.example.delivery.store.repository.StoreRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final UserRepository userRepository;
  private final StoreRepository storeRepository;
  private final OrderRepository orderRepository;

  public void reviewCreate(ReviewRequest request, Long storeId,Long orderId) {

    Order order = orderRepository.findOrderByOrderIdOrElseThrow(orderId);
    // 이미 주문은 유저만 하기에 유저 오너 판별 생략

    // 사라진 메뉴에 대한 리뷰 작성,사라진 가게에 대한 리뷰 작성
    if(order.getMenuId().getIsDeleted || order.getStore().isDeleted() ) {
      throw new NotFoundException(ErrorCode.MENU_NOT_FOUND);
    }

    // 한번에 주문에 하나의 리뷰 작성
    if(reviewRepository.existsByOrder(order)) {
      throw new InvalidRequestException(ErrorCode.REVIEW_REQUEST);
    }

    // 주문한 음식에 한에서 리뷰 작성
    if(!(order.getStore().getId() == storeId)) {
      throw new AccessDeniedException(ErrorCode.REVIEW_ACCESS_DENIED);
    }

    Review review = Review.createReview(order.getUser(), order.getStore(), order,
        request.getContent(), request.getStar());

    reviewRepository.save(review);
  }
}
