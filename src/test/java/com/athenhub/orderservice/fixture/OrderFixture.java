package com.athenhub.orderservice.fixture;

import com.athenhub.orderservice.domain.Order;
import com.athenhub.orderservice.domain.OrderCreateCommand;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Order 도메인 테스트용 Fixture.
 *
 * @author 김지원
 * @since 1.0.0
 */
public class OrderFixture {

  public static OrderCreateCommand validCreateCommand() {
    return new OrderCreateCommand(
        UUID.randomUUID(),
        UUID.randomUUID(),
        "테스터1",
        "slack-id",
        "서울시 강남구",
        "101동 202호",
        LocalDateTime.now().plusDays(1),
        "문 앞에 놔주세요");
  }

  public static OrderCreateCommand pastDueCreateCommand() {
    return new OrderCreateCommand(
        UUID.randomUUID(),
        UUID.randomUUID(),
        "테스터1",
        "slack-id",
        "서울시 강남구",
        "101동 202호",
        LocalDateTime.now().minusDays(1),
        null);
  }

  public static Order create() {
    return Order.create(validCreateCommand());
  }
}
