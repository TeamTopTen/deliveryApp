package org.example.delivery.order.service;

import lombok.RequiredArgsConstructor;
import org.example.delivery.common.domain.Menu;
import org.example.delivery.common.domain.Order;
import org.example.delivery.common.domain.OrderStatus;
import org.example.delivery.common.domain.Store;
import org.example.delivery.common.domain.User;
import org.example.delivery.menu.repository.MenuRepository;
import org.example.delivery.order.model.request.OrderCreateRequest;
import org.example.delivery.order.repository.OrderRepository;
import org.example.delivery.store.repository.StoreRepository;
import org.example.delivery.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final UserRepository userRepository;
  private final StoreRepository storeRepository;
  private final MenuRepository menuRepository;
  private final OrderRepository orderRepository;

  public void createOrder(
      Long storeId,
      Long menuId,
      OrderCreateRequest request) {

    User user = userRepository.findUserByUserIdOrElseThrow(userId);
    Store store = storeRepository.findStoreByStoreIdOrElseThrow(storeId);
    Menu menu = menuRepository.findMenuByMenuIdOrElseThrow(storeId);
    Order order = new Order(user, store, menu, request.getOrderStatus());

    OrderStatus orderStatus = OrderStatus.of(request.getOrderStatus());
    Order order = new Order(orderStatus);
    orderRepository.save(order);
  }
}
