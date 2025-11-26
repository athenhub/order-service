package com.athenhub.orderservice.infrastructure.rabbitmq.publish.order;

import com.athenhub.orderservice.application.dto.OrderCreatedEvent;
import com.athenhub.orderservice.application.event.OrderCreatedEventPublisher;
import com.athenhub.orderservice.infrastructure.rabbitmq.subscribe.RabbitStockProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitStockProperties.class)
public class RabbitOrderCreatedEventPublisher implements OrderCreatedEventPublisher {

  private final RabbitTemplate rabbitTemplate;
  private final RabbitOrderProperties stockProperties;

  @Override
  public void publish(OrderCreatedEvent event) {
    rabbitTemplate.convertAndSend(
        stockProperties.getExchange(), stockProperties.getCreated().getRoutingKey(), event);
  }
}
