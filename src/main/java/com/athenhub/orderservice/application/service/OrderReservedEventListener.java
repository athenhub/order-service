package com.athenhub.orderservice.application.service;

import static com.athenhub.commoncore.error.GlobalErrorCode.NOT_FOUND;

import com.athenhub.orderservice.application.dto.ShippingRequestEvent;
import com.athenhub.orderservice.application.event.ShippingRequestEventPublisher;
import com.athenhub.orderservice.application.exception.OrderServiceException;
import com.athenhub.orderservice.application.service.dto.OrderReservedEvent;
import com.athenhub.orderservice.domain.Order;
import com.athenhub.orderservice.domain.OrderRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 주문 예약 이벤트를 처리하는 리스너이다.
 *
 * <p>{@link OrderReservedEvent}가 발행되면 주문 정보를 조회하고, 배송 상태로 전환한 뒤 {@link ShippingRequestEvent}를 생성하여
 * 외부 배송 서비스로 전달한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderReservedEventListener {

  /** 주문 조회를 위한 저장소. */
  private final OrderRepository orderRepository;

  /** 배송 요청 이벤트 발행자. */
  private final ShippingRequestEventPublisher publisher;

  /**
   * 주문 예약 이벤트를 처리한다.
   *
   * <p>이벤트에 포함된 주문 ID를 기반으로 주문을 조회한 후, 배송 상태로 변경하고 배송 요청 이벤트를 발행한다.
   *
   * @param event 주문 예약 이벤트
   * @author 김지원
   * @since 1.0.0
   */
  @TransactionalEventListener
  public void handle(OrderReservedEvent event) {
    Order order =
        orderRepository
            .findById(event.orderId())
            .orElseThrow(() -> new OrderServiceException(NOT_FOUND, "요청하신 주문을 찾을 수 없습니다."));

    order.shipping(UUID.randomUUID(), LocalDateTime.now());

    ShippingRequestEvent shippingRequestEvent = toShippingRequestEvent(order);
    publisher.publish(shippingRequestEvent);
  }

  /**
   * Order 엔티티를 ShippingRequestEvent로 변환한다.
   *
   * @param order 주문 엔티티
   * @return 배송 요청 이벤트
   * @author 김지원
   * @since 1.0.0
   */
  private ShippingRequestEvent toShippingRequestEvent(Order order) {
    return ShippingRequestEvent.builder()
        .orderId(order.getId().toUuid())
        .sourceHubId(order.getOrderDetails().toList().getFirst().getHubId().toUuid())
        .receiverId(order.getOrderer().getId())
        .receiverName(order.getOrderer().getName())
        .receiverSlackId(order.getOrderer().getSlackId())
        .streetAddress(order.getShippingAddress().getAddress())
        .detailAddress(order.getShippingAddress().getDetailAddress())
        .shippingRequestMemo(order.getShippingRequestMemo())
        .build();
  }
}
