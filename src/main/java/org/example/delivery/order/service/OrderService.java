package org.example.delivery.order.service;

import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.delivery.auth.model.UserRole;
import org.example.delivery.auth.model.dto.AuthUser;
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
import org.example.delivery.order.model.request.OrderCreateRequest;
import org.example.delivery.order.model.request.OrderUpdateRequest;
import org.example.delivery.order.repository.OrderRepository;
import org.example.delivery.store.repository.StoreRepository;
import org.example.delivery.auth.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final UserRepository userRepository;
  private final StoreRepository storeRepository;
  private final OrderRepository orderRepository;

  @Transactional
  public void createOrder(
      AuthUser authUser,
      Long storeId,
      Long menuId,
      OrderCreateRequest request) {
    // 유저가 어떤 유저 인지 판단하는 로직생성
    // 만약 유저가 사업자이면 주문을 못한단고 반환
    //authUser
    Long userId =  authUser.id();
    String userRole =  authUser.userRole().getUserRole();

    if (userRole.equals("owner")){
      throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    User user = userRepository.findById(userId).orElseThrow(() ->
        new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));

    Store store = storeRepository.findById(storeId).orElseThrow(() ->
        new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));

    OrderStatus orderStatus = OrderStatus.of("ORDERED");
    Order order = new Order(user, store, orderStatus);
    System.out.println(order.getStore());
    orderRepository.save(order);
  }

  @Transactional(readOnly = true)
  public Page<OrderPageDto> findOrders(AuthUser authUser, Pageable pageable) {
    // 유저가 어떤 유저 인지 판단하는 로직생성
    // 만약 유저가 사업자이면 주문을 못한단고 반환
    // authUser
    // orderId

    Long userId =  authUser.id(); // 주문한 사람
    String userRole =  authUser.userRole().getUserRole();
    System.out.println(userId);
    System.out.println(userRole);

    if (userRole.equals("owner")){
      return orderRepository.findOrdersByStoreUserId(userId, pageable);
    }

    return orderRepository.findOrdersByUserId(userId, pageable);

  }

  @Transactional(readOnly = true)
  public OrderDto findOrderbyOrderId(AuthUser authUser, Long orderId) {
    // 유저가 어떤 유저 인지 판단하는 로직생성
    // 만약 유저가 사업자이면 주문을 못한단고 반환
    // authUser
    // orderId
    //Long userId = 1L;

    Long userId =  authUser.id();
    String userRole =  authUser.userRole().getUserRole();

    if (userRole.equals("owner")){
      throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    Order order = orderRepository.findOrderByOrderIdOrElseThrow(orderId);

    return new OrderDto(order);

  }

  @Transactional
  public void updateOrderStatus(
      AuthUser authUser,
      Long orderId,
      OrderUpdateRequest request){

    Long userId =  authUser.id();
    String userRole =  authUser.userRole().getUserRole();

    User user = userRepository.findById(userId).orElseThrow(() ->
        new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR));

    Order order = orderRepository.findOrderByOrderIdOrElseThrow(orderId);

    order.changeOrderStatus(OrderStatus.of(request.getOrderStatus()));
  }

  @Transactional
  public void softDeleteOrder(
      AuthUser authUser,
      Long orderId
  ) {
    Order order = orderRepository.findOrderByOrderIdOrElseThrow(orderId);

    order.changeOrderStatus(OrderStatus.of("CANCELLED"));
  }
}
