package com.athenhub.orderservice.infrastructure.rabbitmq.publish.order;

import com.athenhub.orderservice.application.dto.ShippingRequestEvent;
import com.athenhub.orderservice.application.event.ShippingRequestEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 배송 요청 이벤트를 RabbitMQ로 발행하는 퍼블리셔이다.
 *
 * <p>{@link ShippingRequestEventPublisher}의 구현체로, 주문이 배송 상태로 전환될 때 생성되는 {@link
 * ShippingRequestEvent}를 RabbitMQ Exchange로 전송한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class RabbitShippingRequestEventPublisher implements ShippingRequestEventPublisher {

  /** RabbitMQ 메시지 전송을 위한 템플릿. */
  private final RabbitTemplate rabbitTemplate;

  /** Order 관련 RabbitMQ 설정 정보. */
  private final RabbitOrderProperties orderProperties;

  /**
   * 배송 요청 이벤트를 RabbitMQ로 발행한다.
   *
   * <p>설정된 Exchange와 Routing Key를 사용하여 {@link ShippingRequestEvent}를 메시지로 전송한다.
   *
   * @param event 배송 요청 이벤트
   * @author 김지원
   * @since 1.0.0
   */
  @Override
  public void publish(ShippingRequestEvent event) {
    rabbitTemplate.convertAndSend(
        orderProperties.getExchange(), orderProperties.getShipping().getRoutingKey(), event);
  }
}
