package org.example.delivery.order.service;

import lombok.RequiredArgsConstructor;
import org.example.delivery.common.domain.Menu;
import org.example.delivery.common.domain.Order;
import org.example.delivery.common.domain.OrderStatus;
import org.example.delivery.common.domain.Store;
import org.example.delivery.common.domain.User;
import org.example.delivery.menu.repository.MenuRepository;
import org.example.delivery.order.model.dto.OrderDto;
import org.example.delivery.order.model.dto.OrderPageDto;
import org.example.delivery.order.model.request.OrderCreateRequest;
import org.example.delivery.order.model.request.OrderUpdateRequest;
import org.example.delivery.order.repository.OrderRepository;
import org.example.delivery.store.repository.StoreRepository;
import org.example.delivery.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final UserRepository userRepository;
  private final OrderRepository orderRepository;

  @Transactional
  public void createOrder(
      OrderCreateRequest request) {
    // 유저가 어떤 유저 인지 판단하는 로직생성
    // 만약 유저가 사업자이면 주문을 못한단고 반환
    //authUser

//    <Optional>User user = userRepository.findById(1L);

    OrderStatus orderStatus = OrderStatus.of("ORDERED");
    Order order = new Order(orderStatus);
    orderRepository.save(order);
  }

  @Transactional(readOnly = true)
  public Page<OrderPageDto> findOrders(Pageable pageable) {
    // 유저가 어떤 유저 인지 판단하는 로직생성
    // 만약 유저가 사업자이면 주문을 못한단고 반환
    // authUser
    // orderId
    Long userId = 1L;
    return orderRepository.findOrdersByUserId(userId, pageable);

  }

  @Transactional(readOnly = true)
  public OrderDto findOrderbyOrderId(Long orderId) {
    // 유저가 어떤 유저 인지 판단하는 로직생성
    // 만약 유저가 사업자이면 주문을 못한단고 반환
    // authUser
    // orderId
    //Long userId = 1L;
    Order order = orderRepository.findOrderByOrderIdOrElseThrow(orderId);

    return new OrderDto(order);

  }

  @Transactional
  public void updateOrderStatus(
      Long orderId,
      OrderUpdateRequest request){

    Order order = orderRepository.findOrderByOrderIdOrElseThrow(orderId);

    order.changeOrderStatus(OrderStatus.of(request.getOrderStatus()));
  }

  @Transactional
  public void softDeleteOrder(
      Long orderId
  ) {
    Order order = orderRepository.findOrderByOrderIdOrElseThrow(orderId);

    order.changeOrderStatus(OrderStatus.of("CANCELLED"));
  }
}
