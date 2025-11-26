package com.athenhub.orderservice.infrastructure.rabbitmq.subscribe;

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
 * Stock 도메인 관련 RabbitMQ 설정 클래스이다.
 *
 * <p>재고 서비스에서 발생하는 이벤트를 외부로 발행하기 위해 Exchange, Queue, Binding 정보를 정의한다.
 *
 * <p>주요 역할:
 *
 * <ul>
 *   <li>Stock 이벤트용 Topic Exchange 생성
 *   <li>재고 등록 / 감소 이벤트용 Queue 생성
 *   <li>Exchange - Queue 간 Binding 설정
 * </ul>
 *
 * @author 김지원
 * @since 1.0.0
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitStockProperties.class)
public class RabbitStockConfig {

  private final RabbitStockProperties stockProperties;

  /**
   * Stock 관련 이벤트를 발행하는 Topic Exchange.
   *
   * <p>예: stock.exchange
   *
   * @return Stock 이벤트용 TopicExchange
   * @author 김지원
   * @since 1.0.0
   */
  @Bean
  public TopicExchange stockExchange() {
    return new TopicExchange(
        stockProperties.getExchange(), // 예: stock.exchange
        true,
        false);
  }

  @Bean
  public Queue stockDecreasedQueue() {
    return QueueBuilder.durable(stockProperties.getDecreased().getQueue()).build();
  }

  @Bean
  public Binding stockDecreasedBinding(Queue stockDecreasedQueue, TopicExchange stockExchange) {
    return BindingBuilder.bind(stockDecreasedQueue)
        .to(stockExchange)
        .with(stockProperties.getDecreased().getRoutingKey());
  }

  @Bean
  public Queue stockDecreaseFailQueue() {
    return QueueBuilder.durable(stockProperties.getDecreaseFail().getQueue()).build();
  }

  @Bean
  public Binding stockDecreaseFailBinding(
      Queue stockDecreaseFailQueue, TopicExchange stockExchange) {
    return BindingBuilder.bind(stockDecreaseFailQueue)
        .to(stockExchange)
        .with(stockProperties.getDecreaseFail().getRoutingKey());
  }
}
