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

  @Pointcut("execution(* org.example.delivery.order.service.OrderService.createOrder(..)) || " +
      "execution(* org.example.delivery.order.service.OrderService.updateOrderStatus(..)) || " +
      "execution(* org.example.delivery.order.service.OrderService.softDeleteOrder(..))")
  public void orderServiceMethods() {}

  @After("orderServiceMethods() && args(authUser, storeId, orderId, ..)")
  public void logOrderRequest(AuthUser authUser, Long storeId, Long orderId) {
    String userRole = authUser.userRole().getUserRole();
    String action = "";

    if (orderId != null) {
      if (userRole.equals("owner")) {
        action = "주문 상태 변경";
      } else {
        action = "주문 요청";
      }
    }

    log.info("[" + LocalDateTime.now() + "] " +
        "Action: " + action + " | " +
        "UserRole: " + userRole + " | " +
        "StoreId: " + storeId + " | " +
        "OrderId: " + orderId);
  }

}