package com.athenhub.orderservice.application.service;

import static com.athenhub.commoncore.error.GlobalErrorCode.*;

import com.athenhub.orderservice.application.exception.OrderServiceException;
import com.athenhub.orderservice.domain.Order;
import com.athenhub.orderservice.domain.OrderRepository;
import com.athenhub.orderservice.domain.vo.OrderId;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderUpdateService {

  private final OrderRepository orderRepository;

  public void updateReserved(UUID orderId, LocalDateTime reservedAt) {
    Order order =
        orderRepository
            .findById(OrderId.of(orderId))
            .orElseThrow(() -> new OrderServiceException(NOT_FOUND, "요청하신 주문을 찾을 수 없습니다."));
    order.reserved(reservedAt);
  }

  public void updateCancel(UUID orderId, String cancelMessage, LocalDateTime cancelAt) {
    Order order =
        orderRepository
            .findById(OrderId.of(orderId))
            .orElseThrow(() -> new OrderServiceException(NOT_FOUND, "요청하신 주문을 찾을 수 없습니다."));
    order.cancel(cancelMessage, cancelAt);
  }
}
