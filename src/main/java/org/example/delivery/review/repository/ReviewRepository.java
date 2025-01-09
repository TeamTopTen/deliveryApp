package org.example.delivery.review.repository;

import org.example.delivery.common.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  boolean existsByOrderId(Long orderId);
}
