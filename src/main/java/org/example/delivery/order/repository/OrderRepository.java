package org.example.delivery.order.repository;

import java.util.Optional;
import org.example.delivery.common.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface OrderRepository extends JpaRepository<Order, Long> {

  Optional<Order> findByMenuId(Long menuId);

  default Order findByMenuIdOrElseThrow(Long menuId) {
    return findByMenuId(menuId).orElseThrow(() ->
        new ResponseStatusException(HttpStatus.NOT_FOUND, "Does not exist menuId = " + menuId));
  }
}
