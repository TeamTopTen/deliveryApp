package org.example.delivery.common.aop;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.delivery.common.domain.entity.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class OrderStatusChangeLoggingAspect {

  @Pointcut("execution(* org.example.delivery.order.repository.OrderRepository.save(..)) && args(order)")
  public void saveOrderMethod(Order order) {
  }

  @After("saveOrderMethod(order)")
  public void logAfterOrderSave(Order order) {
    log.info("[" + LocalDateTime.now() + "] " +
        "OrderStatus: " + order.getOrderStatus() + " | " +
        "OrderId: " + order.getId() + " | " +
        "StoreId: " + order.getStore().getId() + " | " +
        "MenuId: " + order.getMenu().getId() + " | " +
        "UserId: " + order.getUser().getId());
  }
}