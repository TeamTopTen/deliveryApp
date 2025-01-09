package org.example.delivery.order.service;

import java.sql.Time;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.example.delivery.auth.model.dto.AuthUser;
import org.example.delivery.auth.repository.UserRepository;
import org.example.delivery.common.domain.Menu;
import org.example.delivery.common.domain.Order;
import org.example.delivery.common.domain.OrderStatus;
import org.example.delivery.common.domain.Store;
import org.example.delivery.common.domain.User;
import org.example.delivery.common.exception.ErrorCode;
import org.example.delivery.common.exception.base.BusinessException;
import org.example.delivery.menu.repository.MenuRepository;
import org.example.delivery.order.model.dto.OrderDto;
import org.example.delivery.order.model.dto.OrderPageDto;
import org.example.delivery.order.model.request.OrderUpdateRequest;
import org.example.delivery.order.repository.OrderRepository;
import org.example.delivery.store.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final UserRepository userRepository;
  private final StoreRepository storeRepository;
  private final MenuRepository menuRepository;
  private final OrderRepository orderRepository;

  @Transactional
  public void createOrder(
      AuthUser authUser,
      Long storeId,
      Long menuId) {

    String userRole = authUser.userRole().getUserRole();
    Long userId = authUser.id();

    if (userRole.equals("owner")) {
      throw new BusinessException(ErrorCode.ORDER_ACCESS_DENIED); // user만 등록가능
    }

    User user = userRepository.findById(userId).orElseThrow(() ->
        new BusinessException(ErrorCode.USER_NOT_FOUND));

    Store store = storeRepository.findById(storeId).orElseThrow(() ->
        new BusinessException(ErrorCode.STORE_NOT_FOUND));

    if (LocalTime.now().isBefore(store.getOpeningTime().toLocalTime()) ||
        LocalTime.now().isAfter(store.getClosingTime().toLocalTime())) {
      throw new BusinessException(ErrorCode.ORDER_TIME_BAD_REQUEST);
    }

    Menu menu = menuRepository.findById(menuId).orElseThrow(() ->
        new BusinessException(ErrorCode.MENU_NOT_FOUND));

    if (menu.getPrice() < store.getMinOrderPrice()){
      throw new BusinessException(ErrorCode.ORDER_MIN_PRICE_BAD_REQUEST);
    }

    OrderStatus orderStatus = OrderStatus.of("ORDERED");

    Order order = new Order(user, store, menu, orderStatus);

    orderRepository.save(order);
  }

  @Transactional(readOnly = true)
  public Page<OrderPageDto> findOrders(AuthUser authUser, Pageable pageable) {

    Long userId = authUser.id();
    String userRole = authUser.userRole().getUserRole();
    // 로그인 한사람이 owner이면

    if (userRole.equals("owner")) {
      return orderRepository.findOrdersByStoreUserId(userId, pageable);
    }

    return orderRepository.findOrdersByUserId(userId, pageable);

  }


  @Transactional(readOnly = true)
  public OrderDto findOrderbyOrderId(AuthUser authUser, Long orderId) {

    Long userId = authUser.id();

    String userRole = authUser.userRole().getUserRole();

    if (userRole.equals("owner")) {
      Order order = orderRepository.findOrderByStoreUserIdAndOrderIdOrElseThrow(userId, orderId);
      return new OrderDto(order);
    }

    Order order = orderRepository.findOrderByUserIdAndOrderIdOrElseThrow(userId, orderId);
    return new OrderDto(order);
  }

  @Transactional
  public void updateOrderStatus(
      AuthUser authUser,
      Long orderId,
      OrderUpdateRequest request) {

    Long userId = authUser.id();

    String userRole = authUser.userRole().getUserRole();

    OrderStatus status = OrderStatus.of(request.getOrderStatus());

    if (userRole.equals("owner")) {
      Order order = orderRepository.findOrderByStoreUserIdAndOrderIdOrElseThrow(userId, orderId);
      order.changeOrderStatus(status);
    }

    Order order = orderRepository.findOrderByUserIdAndOrderIdOrElseThrow(userId, orderId);

    order.changeOrderStatus(status);

    orderRepository.save(order);
  }

  @Transactional
  public void softDeleteOrder(
      AuthUser authUser,
      Long orderId
  ) {
    Long userId = authUser.id();

    String userRole = authUser.userRole().getUserRole();

    if (userRole.equals("owner")) {
      throw new BusinessException(ErrorCode.ORDER_ACCESS_DENIED);
    }

    Order order = orderRepository.findOrderByUserIdAndOrderIdOrElseThrow(userId, orderId);

    order.changeOrderStatus(OrderStatus.of("CANCELLED"));

    orderRepository.save(order);
  }
}
