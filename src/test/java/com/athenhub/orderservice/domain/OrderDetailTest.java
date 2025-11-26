package com.athenhub.orderservice.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.athenhub.orderservice.domain.vo.Price;
import com.athenhub.orderservice.fixture.OrderDetailFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link OrderDetail} 도메인 테스트.
 *
 * @author 김지원
 * @since 1.0.0
 */
class OrderDetailTest {

  @Test
  @DisplayName("주문 상세 생성 시 총 금액이 올바르게 계산된다.")
  void getTotalPrice_success() {
    OrderDetail orderDetail = OrderDetailFixture.create(2_000L, 3);
    assertThat(orderDetail.getTotalPrice()).isEqualTo(Price.of(6_000L));
  }
}
