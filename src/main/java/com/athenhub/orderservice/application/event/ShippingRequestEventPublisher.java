package com.athenhub.orderservice.application.event;

import com.athenhub.orderservice.application.dto.ShippingRequestEvent;

/**
 * 배송 요청 이벤트를 발행하기 위한 퍼블리셔 인터페이스이다.
 *
 * <p>구현체는 메시지 브로커(RabbitMQ, Kafka 등)를 통해 {@link ShippingRequestEvent}를 외부 시스템에 전달하는 역할을 한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
public interface ShippingRequestEventPublisher {

  /**
   * 배송 요청 이벤트를 발행한다.
   *
   * @param event 배송 요청 이벤트
   * @author 김지원
   * @since 1.0.0
   */
  void publish(ShippingRequestEvent event);
}
