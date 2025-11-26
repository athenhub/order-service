package com.athenhub.orderservice.application.event;

import com.athenhub.orderservice.application.dto.OrderCreatedEvent;

public interface OrderCreatedEventPublisher {

  void publish(OrderCreatedEvent event);
}
