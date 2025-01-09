package org.example.delivery.review.repository;

import java.util.List;
import org.example.delivery.common.domain.Order;
import org.example.delivery.common.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {

  boolean existsByOrder(Order order);
}
