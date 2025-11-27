package com.athenhub.orderservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.athenhub.orderservice.domain.vo.OrderDetailId;
import com.athenhub.orderservice.domain.vo.Price;
import com.athenhub.orderservice.fixture.OrderDetailFixture;
import com.athenhub.orderservice.fixture.OrderFixture;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link Order} 도메인 테스트이다.
 *
 * @author 김지원
 * @since 1.0.0
 */
class OrderTest {

  @Test
  @DisplayName("주문 생성 시 기본 상태와 값이 올바르게 초기화된다.")
  void createOrder() {
    Order order = OrderFixture.create();

    assertThat(order.getId()).isNotNull();

    assertThat(order.getOrderer()).isNotNull();
    assertThat(order.getOrderer().getId()).isNotNull();
    assertThat(order.getOrderer().getSlackId()).isNotNull();
    assertThat(order.getOrderer().getName()).isNotNull();
    assertThat(order.getShippingId()).isNull();

    assertThat(order.getStatus()).isEqualTo(OrderStatus.CREATED);
    assertThat(order.getTotalPrice()).isEqualTo(Price.ZERO);
  }

  @Test
  @DisplayName("과거 시점의 배송 기한으로 주문 생성 시 예외가 발생한다.")
  void createOrder_withPastShippingDueAt_shouldFail() {
    assertThatThrownBy(() -> Order.create(OrderFixture.pastDueCreateCommand()))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("배송 기한");
  }

  @Test
  @DisplayName("주문 상세 추가 시 총 금액이 갱신된다.")
  void addOrderDetail_updatesTotalPrice() {
    Order order = OrderFixture.create();
    OrderDetail orderDetail = OrderDetailFixture.create(1_000L, 2);

    order.addOrderDetail(orderDetail);

    assertThat(order.getTotalPrice()).isEqualTo(Price.of(2_000L));
  }

  @Test
  @DisplayName("주문 상태가 SHIPPING으로 변경된다.")
  void shipping_success() {
    Order order = OrderFixture.create();

    order.shipping(UUID.randomUUID(), LocalDateTime.now());

    assertThat(order.getStatus()).isEqualTo(OrderStatus.SHIPPING);
    assertThat(order.getShippingId()).isNotNull();
    assertThat(order.getShippedAt()).isNotNull();
  }

  @Test
  @DisplayName("주문 상태가 COMPLETED로 변경된다.")
  void completed_success() {
    Order order = OrderFixture.create();

    order.completed(LocalDateTime.now());

    assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    assertThat(order.getCompletedAt()).isNotNull();
  }

  @Test
  @DisplayName("COMPLETED 상태의 주문은 상태 변경이 불가능하다.")
  void changeStatus_afterCompleted_shouldFail() {
    Order order = OrderFixture.create();
    order.completed(LocalDateTime.now());

    assertThatThrownBy(() -> order.cancel("", LocalDateTime.now()))
        .isInstanceOf(IllegalStateException.class)
        .hasMessageContaining("상태를 변경할 수 없습니다.");
  }

  @Test
  @DisplayName("주문 상세를 ID로 조회할 수 있다.")
  void getOrderDetail_success() {
    Order order = OrderFixture.create();
    OrderDetail orderDetail = OrderDetailFixture.create(5_000L, 1);

    order.addOrderDetail(orderDetail);
    OrderDetailId targetId = orderDetail.getId();

    OrderDetail found = order.getOrderDetail(targetId);

    assertThat(found.getId()).isEqualTo(targetId);
  }
}
