package com.athenhub.orderservice.fixture;

import com.athenhub.orderservice.domain.OrderDetail;
import com.athenhub.orderservice.domain.dto.OrderDetailCreateCommand;
import java.util.UUID;

/**
 * OrderDetail 도메인 테스트용 Fixture.
 *
 * @author 김지원
 * @since 1.0.0
 */
public class OrderDetailFixture {

  public static OrderDetailCreateCommand createCommand(long unitPrice, int quantity) {
    return new OrderDetailCreateCommand(
        UUID.randomUUID(),
        UUID.randomUUID(),
        UUID.randomUUID(),
        "name",
        UUID.randomUUID(),
        "variantName",
        unitPrice,
        quantity);
  }

  public static OrderDetail create(long price, int quantity) {
    return OrderDetail.create(createCommand(price, quantity));
  }

  public static OrderDetail create() {
    return create(1_000L, 1);
  }
}
