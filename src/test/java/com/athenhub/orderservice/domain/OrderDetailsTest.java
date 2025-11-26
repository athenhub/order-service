package com.athenhub.orderservice.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.athenhub.commoncore.error.GlobalErrorCode;
import com.athenhub.orderservice.domain.exception.OrderDomainException;
import com.athenhub.orderservice.domain.vo.OrderDetailId;
import com.athenhub.orderservice.domain.vo.Price;
import com.athenhub.orderservice.fixture.OrderDetailFixture;
import com.athenhub.orderservice.fixture.OrderFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@link OrderDetails} 도메인 테스트이다.
 *
 * <p>OrderDetails의 주요 책임(추가, 조회, 금액 합산, 예외 처리)을 검증한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
class OrderDetailsTest {

  @Test
  @DisplayName("OrderDetails에 주문 상세를 추가하면 총 금액이 합산된다.")
  void addAndCalculateTotalPrice() {

    Order order = OrderFixture.create();

    OrderDetail detail1 = OrderDetailFixture.create(1_000L, 2); // 2,000
    OrderDetail detail2 = OrderDetailFixture.create(3_000L, 1); // 3,000

    order.addOrderDetail(detail1);
    order.addOrderDetail(detail2);

    Price total = order.getTotalPrice();

    assertThat(total).isEqualTo(Price.of(5_000L));
  }

  @Test
  @DisplayName("OrderDetails에서 식별자로 주문 상세를 조회할 수 있다.")
  void getOrderDetailById() {

    Order order = OrderFixture.create();

    OrderDetail detail = OrderDetailFixture.create(2_000L, 1);
    order.addOrderDetail(detail);

    OrderDetailId targetId = detail.getId();

    OrderDetail found = order.getOrderDetail(targetId);

    assertThat(found).isNotNull();
    assertThat(found.getId()).isEqualTo(targetId);
  }

  @Test
  @DisplayName("존재하지 않는 주문 상세를 조회하면 예외가 발생한다.")
  void getOrderDetail_notFound() {

    Order order = OrderFixture.create();

    OrderDetailId notExistId = OrderDetailId.create();

    assertThatThrownBy(() -> order.getOrderDetail(notExistId))
        .isInstanceOf(OrderDomainException.class)
        .hasFieldOrPropertyWithValue("errorCode", GlobalErrorCode.NOT_FOUND);
  }

  @Test
  @DisplayName("주문 상세 목록은 외부에서 수정할 수 없는 불변 리스트를 반환한다.")
  void getValues_returnsImmutableList() {

    Order order = OrderFixture.create();

    OrderDetail detail = OrderDetailFixture.create(1_000L, 1);
    order.addOrderDetail(detail);

    assertThatThrownBy(() -> order.getOrderDetails().getValues().add(detail))
        .isInstanceOf(UnsupportedOperationException.class);
  }
}
