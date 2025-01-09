package org.example.delivery.common.aop;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.example.delivery.auth.model.dto.AuthUser;
import org.example.delivery.common.domain.Order;
import org.example.delivery.common.domain.OrderStatus;
import org.example.delivery.order.model.request.OrderUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class OrderStatusChangeLoggingAspect {

  // @Pointcut 설정 - OrderService의 메서드에서 orderRepository.save() 호출 시
  @Pointcut("execution(* org.example.delivery.order.repository.OrderRepository.save(..)) && args(order)")
  public void saveOrderMethod(Order order) {}

  // @Before 어노테이션을 사용하여 save 메서드 호출 직전에 order 로그 남기기
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