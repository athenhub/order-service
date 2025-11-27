package com.athenhub.orderservice.infrastructure.rabbitmq.subscribe;

import com.athenhub.orderservice.application.service.OrderUpdateService;
import com.athenhub.orderservice.infrastructure.rabbitmq.subscribe.dto.StockDecreasedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockDecreasedEventListener {

  private final OrderUpdateService orderUpdateService;

  @RabbitListener(queues = "${rabbit.stock.decreased.queue}")
  public void listen(StockDecreasedEvent event) {
    orderUpdateService.updateReserved(event.orderId(), event.decreasedAt());
  }
}
