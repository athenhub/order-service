package com.athenhub.orderservice.infrastructure.rabbitmq.publish.order;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Order 도메인 관련 RabbitMQ 설정 클래스이다.
 *
 * <p>Order 서비스에서 발생하는 이벤트를 외부 서비스(예: 배송, 재고 등)가 구독할 수 있도록 Exchange, Queue, Binding 정보를 정의한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitOrderProperties.class)
public class RabbitOrderConfig {

  /** Order 관련 RabbitMQ 설정 프로퍼티. */
  private final RabbitOrderProperties orderProperties;

  /**
   * 주문(Order) 관련 이벤트를 송수신하기 위한 Topic Exchange.
   *
   * <p>예: order.exchange
   *
   * @return 주문 이벤트용 TopicExchange
   * @author 김지원
   * @since 1.0.0
   */
  @Bean
  public TopicExchange orderExchange() {
    return new TopicExchange(
        orderProperties.getExchange(), // 예: order.exchange
        true,
        false);
  }

  /**
   * 주문 생성 이벤트를 수신하는 Queue.
   *
   * <p>예: order.created.queue
   *
   * @return 주문 생성 이벤트 수신용 Queue
   * @author 김지원
   * @since 1.0.0
   */
  @Bean
  public Queue orderCreatedQueue() {
    return QueueBuilder.durable(orderProperties.getCreated().getQueue()).build();
  }

  /**
   * 주문 생성 이벤트용 Binding.
   *
   * <p>order.exchange 에서 order.created 라우팅 키로 들어오는 메시지를 order.created.queue 로 전달한다.
   *
   * @param orderCreatedQueue 주문 생성 이벤트 Queue
   * @param orderExchange 주문 이벤트용 Topic Exchange
   * @return Queue 와 Exchange 를 연결하는 Binding
   * @author 김지원
   * @since 1.0.0
   */
  @Bean
  public Binding orderCreateBinding(Queue orderCreatedQueue, TopicExchange orderExchange) {
    return BindingBuilder.bind(orderCreatedQueue)
        .to(orderExchange)
        .with(orderProperties.getCreated().getRoutingKey());
  }

  /**
   * 주문 배송(Shipping) 요청 이벤트를 수신하는 Queue.
   *
   * <p>예: order.shipping.queue
   *
   * @return 주문 배송 이벤트 수신용 Queue
   * @author 김지원
   * @since 1.0.0
   */
  @Bean
  public Queue orderShippingQueue() {
    return QueueBuilder.durable(orderProperties.getShipping().getQueue()).build();
  }

  /**
   * 주문 배송(Shipping) 이벤트용 Binding.
   *
   * <p>order.exchange 에서 order.shipping 라우팅 키로 들어오는 메시지를 order.shipping.queue 로 전달한다.
   *
   * @param orderShippingQueue 주문 배송 이벤트 Queue
   * @param orderExchange 주문 이벤트용 Topic Exchange
   * @return Queue 와 Exchange 를 연결하는 Binding
   * @author 김지원
   * @since 1.0.0
   */
  @Bean
  public Binding orderShippingBinding(Queue orderShippingQueue, TopicExchange orderExchange) {
    return BindingBuilder.bind(orderShippingQueue)
        .to(orderExchange)
        .with(orderProperties.getShipping().getRoutingKey());
  }
}
