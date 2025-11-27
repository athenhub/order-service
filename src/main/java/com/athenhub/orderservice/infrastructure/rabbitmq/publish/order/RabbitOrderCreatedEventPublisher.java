package com.athenhub.orderservice.infrastructure.rabbitmq.publish.order;

import com.athenhub.orderservice.application.dto.OrderCreatedEvent;
import com.athenhub.orderservice.application.event.OrderCreatedEventPublisher;
import com.athenhub.orderservice.infrastructure.rabbitmq.subscribe.RabbitStockProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 주문 생성 이벤트를 RabbitMQ로 발행하는 퍼블리셔이다.
 *
 * <p>{@link OrderCreatedEventPublisher} 인터페이스의 구현체로, Order 서비스에서 발생한 주문 생성 이벤트를 RabbitMQ Exchange로
 * 전송하는 역할을 한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitStockProperties.class)
public class RabbitOrderCreatedEventPublisher implements OrderCreatedEventPublisher {

  /** RabbitMQ 메시지 전송을 위한 템플릿. */
  private final RabbitTemplate rabbitTemplate;

  /** Order 관련 RabbitMQ 설정 정보. */
  private final RabbitOrderProperties orderProperties;

  /**
   * 주문 생성 이벤트를 RabbitMQ로 발행한다.
   *
   * <p>설정된 Exchange와 Routing Key를 사용하여 {@link OrderCreatedEvent}를 메시지로 전송한다.
   *
   * @param event 주문 생성 이벤트
   * @author 김지원
   * @since 1.0.0
   */
  @Override
  public void publish(OrderCreatedEvent event) {
    rabbitTemplate.convertAndSend(
        orderProperties.getExchange(), orderProperties.getCreated().getRoutingKey(), event);
  }
}
