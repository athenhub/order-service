package com.athenhub.orderservice.infrastructure.rabbitmq.subscribe;

import com.athenhub.orderservice.application.service.OrderUpdateService;
import com.athenhub.orderservice.infrastructure.rabbitmq.subscribe.dto.StockDecreasedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockDecreaseFailEventListener {

  private final OrderUpdateService orderUpdateService;

  @RabbitListener(queues = "${rabbit.stock.decrease-fail.queue}")
  public void listen(StockDecreasedEvent event) {
    orderUpdateService.updateCancel(event.orderId(), "재고 차감 충돌", event.decreasedAt());
  }
}
