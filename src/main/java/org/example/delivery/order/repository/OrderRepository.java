package org.example.delivery.order.repository;

import java.util.Optional;
import org.example.delivery.auth.model.UserRole;
import org.example.delivery.common.domain.Order;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.BusinessException;
import org.example.delivery.order.model.dto.OrderDto;
import org.example.delivery.order.model.dto.OrderPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

  @Query("SELECT new org.example.delivery.order.model.dto.OrderPageDto(" +
      "o.id, o.user.name, o.store.name, o.menu.name," +
      "o.menu.price, o.orderStatus, " +
      "o.createdAt, o.updatedAt) " +
      "FROM Order o " +
      "WHERE (o.store.user.id = :storeUserId) " +
      "ORDER BY o.updatedAt DESC")
  Page<OrderPageDto> findOrdersByStoreUserId(@Param("storeUserId") Long storeUserId,
                                             Pageable pageable);


  @Query("SELECT new org.example.delivery.order.model.dto.OrderPageDto(" +
      "o.id, o.user.name, o.store.name, o.menu.name," +
      "o.menu.price, o.orderStatus, " +
      "o.createdAt, o.updatedAt) " +
      "FROM Order o " +
      "WHERE (o.user.id = :userId) " +
      "ORDER BY o.updatedAt DESC")
  Page<OrderPageDto> findOrdersByUserId(@Param("userId") Long userId,
                                        Pageable pageable);


  @Query("SELECT o " +
      "FROM Order o " +
      "WHERE o.store.user.id = :storeUserId and o.id = :orderId")
  Optional<Order> findOrderByStoreUserIdAndOrderId(
      @Param("storeUserId") Long storeUserId,
      @Param("orderId") Long orderId);

  default Order findOrderByStoreUserIdAndOrderIdOrElseThrow(Long storeUserId, Long orderId) {
    return findOrderByStoreUserIdAndOrderId(storeUserId,orderId)
        .orElseThrow(() ->
            new BusinessException(ErrorCode.ORDER_NOT_FOUND));
  }


  @Query("SELECT o " +
      "FROM Order o " +
      "WHERE o.user.id = :userId and o.id = :orderId")
  Optional<Order> findOrderByUserIdAndOrderId(
      @Param("userId") Long userId,
      @Param("orderId") Long orderId);

  default Order findOrderByUserIdAndOrderIdOrElseThrow(Long userId, Long orderId) {
    return findOrderByUserIdAndOrderId(userId,orderId)
        .orElseThrow(() ->
            new BusinessException(ErrorCode.ORDER_NOT_FOUND));
  }

}
