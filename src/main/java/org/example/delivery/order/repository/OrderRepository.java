package org.example.delivery.order.repository;

import java.util.Optional;
import org.example.delivery.common.domain.Order;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.BusinessException;
import org.example.delivery.order.model.dto.OrderPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface OrderRepository extends JpaRepository<Order, Long> {
  @Query("SELECT new org.example.delivery.order.model.dto.OrderPageDto(" +
      "o.id, o.orderStatus, " +
      "o.createdAt, o.updatedAt) " +
      "FROM Order o " +
      "WHERE (o.user.id = :userId) " +
      "ORDER BY o.updatedAt DESC")
  Page<OrderPageDto> findOrdersByUserId(@Param("userId") Long userId, Pageable pageable);

  @Query("SELECT new org.example.delivery.order.model.dto.OrderPageDto(" +
      "o.id, o.orderStatus, " +
      "o.createdAt, o.updatedAt) " +
      "FROM Order o " +
      "WHERE o.store.user.id = :userId " + // Store를 통해 User의 id를 조회
      "ORDER BY o.updatedAt DESC")
  Page<OrderPageDto> findOrdersByStoreUserId(@Param("userId") Long userId, Pageable pageable);


  Optional<Order> findOrderByUserId(Long userId);

  default Order findOrderByUserIdOrElseThrow(Long userId) {
    return findOrderByUserId(userId)
        .orElseThrow(() ->
            new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
  }


  Optional<Order> findOrderById(Long orderId);

  default Order findOrderByOrderIdOrElseThrow(Long orderId) {
    return findOrderById(orderId)
        .orElseThrow(() ->
        new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
  }

  Optional<Order> findOrderByStoreId(Long storeId);

  default Order findOrderByStoreIdOrElseThrow(Long storeId) {
    return findOrderByStoreId(storeId)
        .orElseThrow(() ->
            new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));
  }
}
