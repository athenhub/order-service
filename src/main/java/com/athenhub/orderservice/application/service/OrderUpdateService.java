package com.athenhub.orderservice.application.service;

import static com.athenhub.commoncore.error.GlobalErrorCode.NOT_FOUND;

import com.athenhub.orderservice.application.exception.OrderServiceException;
import com.athenhub.orderservice.application.service.dto.OrderReservedEvent;
import com.athenhub.orderservice.domain.Order;
import com.athenhub.orderservice.domain.OrderRepository;
import com.athenhub.orderservice.domain.vo.OrderId;
import com.athenhub.orderservice.global.event.Events;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문 상태 변경(예약, 취소)을 처리하는 서비스이다.
 *
 * <p>주문 상태 변경 후에는 도메인 이벤트를 발행하여 후속 처리를 위임한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OrderUpdateService {

  /** 주문 조회 및 저장을 담당하는 리포지토리. */
  private final OrderRepository orderRepository;

  /**
   * 주문 상태를 예약(reserved)으로 변경한다.
   *
   * <p>주문 조회 후 예약 시각을 설정하고, {@link OrderReservedEvent}를 발행하여 후속 프로세스(예: 배송 요청)를 트리거한다.
   *
   * @param orderId 예약할 주문 ID
   * @param reservedAt 예약 시각
   * @author 김지원
   * @since 1.0.0
   */
  public void updateReserved(UUID orderId, LocalDateTime reservedAt) {
    Order order =
        orderRepository
            .findById(OrderId.of(orderId))
            .orElseThrow(() -> new OrderServiceException(NOT_FOUND, "요청하신 주문을 찾을 수 없습니다."));

    order.reserved(reservedAt);

    Events.trigger(new OrderReservedEvent(order.getId()));
  }

  /**
   * 주문 상태를 취소(cancel)로 변경한다.
   *
   * <p>주문을 조회한 후 취소 사유와 취소 시각을 설정한다.
   *
   * @param orderId 취소할 주문 ID
   * @param cancelMessage 취소 사유
   * @param cancelAt 취소 시각
   * @author 김지원
   * @since 1.0.0
   */
  public void updateCancel(UUID orderId, String cancelMessage, LocalDateTime cancelAt) {
    Order order =
        orderRepository
            .findById(OrderId.of(orderId))
            .orElseThrow(() -> new OrderServiceException(NOT_FOUND, "요청하신 주문을 찾을 수 없습니다."));

    order.cancel(cancelMessage, cancelAt);
  }
}
