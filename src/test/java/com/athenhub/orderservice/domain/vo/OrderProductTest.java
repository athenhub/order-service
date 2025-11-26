package com.athenhub.orderservice.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.athenhub.orderservice.domain.dto.OrderProductCreateCommand;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link OrderProduct} 도메인 테스트.
 *
 * @author 김지원
 * @since 1.0.0
 */
class OrderProductTest {

  @Test
  @DisplayName("정상적인 값으로 OrderProduct를 생성할 수 있다.")
  void create_success() {
    OrderProductCreateCommand command =
        new OrderProductCreateCommand(
            UUID.randomUUID(), "name", UUID.randomUUID(), "variant", 1_000L, 2);

    OrderProduct orderProduct = OrderProduct.create(command);

    assertThat(orderProduct).isNotNull();
    assertThat(orderProduct.getTotalPrice()).isEqualTo(Price.of(2_000L));
  }

  @Test
  @DisplayName("수량이 0 이하이면 예외가 발생한다.")
  void create_withInvalidQuantity_shouldFail() {
    OrderProductCreateCommand command =
        new OrderProductCreateCommand(
            UUID.randomUUID(), "name", UUID.randomUUID(), "variant", 1_000L, 0);

    assertThatThrownBy(() -> OrderProduct.create(command))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("주문 수량은 1 이상");
  }

  @Test
  @DisplayName("단가가 0 이하이면 예외가 발생한다.")
  void create_withInvalidUnitPrice_shouldFail() {
    OrderProductCreateCommand command =
        new OrderProductCreateCommand(
            UUID.randomUUID(), "name", UUID.randomUUID(), "variant", 0, 1);

    assertThatThrownBy(() -> OrderProduct.create(command))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("단가는 0보다");
  }

  @Test
  @DisplayName("총 금액은 단가 * 수량으로 계산된다.")
  void calculateTotalPrice() {
    OrderProductCreateCommand command =
        new OrderProductCreateCommand(
            UUID.randomUUID(), "name", UUID.randomUUID(), "variant", 5_000L, 3);

    OrderProduct orderProduct = OrderProduct.create(command);

    assertThat(orderProduct.getTotalPrice()).isEqualTo(Price.of(15_000L));
  }
}
