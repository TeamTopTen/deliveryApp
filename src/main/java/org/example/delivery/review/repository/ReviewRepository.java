package org.example.delivery.review.repository;

import org.example.delivery.common.domain.Review;
import org.example.delivery.review.model.dto.ReviewPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
  boolean existsByOrderId(Long orderId);

  @Query(
      "SELECT new org.example.delivery.review.model.dto.ReviewPageDto(" +
          "r.id, " +
          "r.order.menu.name, " +
          "r.order.menu.price, " +
          "r.reviewStar, " +
          "r.content, " +
          "r.createdAt, " +
          "r.updatedAt" +
          ") " +
          "FROM Review r " +
          "WHERE r.order.store.id = :storeId "+
          "ORDER BY r.updatedAt DESC"
  )
  Page<ReviewPageDto> findReviewsByStoreId(@Param("storeId") Long storeId, Pageable pageable);
}
