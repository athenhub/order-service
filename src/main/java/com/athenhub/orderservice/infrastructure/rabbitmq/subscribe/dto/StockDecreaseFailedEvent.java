package com.athenhub.orderservice.infrastructure.rabbitmq.subscribe.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record StockDecreaseFailedEvent(UUID orderId, String message, LocalDateTime failedAt) {

  public static StockDecreaseFailedEvent of(UUID orderId, String message) {
    return new StockDecreaseFailedEvent(orderId, message, LocalDateTime.now());
  }
}
