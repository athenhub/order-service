package com.athenhub.orderservice.domain;

import com.athenhub.orderservice.domain.dto.OrderDetailCreateCommand;
import com.athenhub.orderservice.domain.vo.HubId;
import com.athenhub.orderservice.domain.vo.OrderDetailId;
import com.athenhub.orderservice.domain.vo.OrderProduct;
import com.athenhub.orderservice.domain.vo.Price;
import com.athenhub.orderservice.domain.vo.VendorId;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 주문 상세(OrderDetail) 엔티티이다.
 *
 * <p>하나의 주문(Order)에 포함되는 개별 상품 단위를 나타낸다. 공급자, 허브, 상품 정보, 수량, 금액 계산에 필요한 정보를 포함한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@Entity
@Table(name = "p_order_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class OrderDetail {

  /** 주문 상세 식별자. */
  @EmbeddedId private OrderDetailId id;

  /** 상품 공급자(업체) 식별자. */
  @Embedded private VendorId producerId;

  /** 상품이 소속된 허브 식별자. */
  @Embedded private HubId hubId;

  /** 주문된 상품 정보(상품 ID, 옵션 ID, 단가, 수량 등). */
  @Embedded private OrderProduct orderProduct;

  /** 연결된 상위 주문 엔티티. */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private Order order;

  /**
   * OrderDetail 생성자이다.
   *
   * <p>외부에서 직접 호출하지 않으며 {@link #create(OrderDetailCreateCommand)} 를 통해 생성된다.
   *
   * @param id 주문 상세 식별자
   * @param producerId 공급자 식별자
   * @param hubId 허브 식별자
   * @param orderProduct 주문 상품 정보
   */
  private OrderDetail(
      OrderDetailId id, VendorId producerId, HubId hubId, OrderProduct orderProduct) {

    this.id = Objects.requireNonNull(id);
    this.producerId = Objects.requireNonNull(producerId);
    this.hubId = Objects.requireNonNull(hubId);
    this.orderProduct = Objects.requireNonNull(orderProduct);
  }

  /**
   * OrderDetail을 생성한다.
   *
   * <p>외부 커맨드 객체를 도메인 객체로 변환하여 생성한다.
   *
   * @param command 주문 상세 생성을 위한 커맨드
   * @return 생성된 OrderDetail
   */
  public static OrderDetail create(OrderDetailCreateCommand command) {
    return new OrderDetail(
        OrderDetailId.create(),
        VendorId.of(command.producerId()),
        HubId.of(command.hubId()),
        OrderProduct.create(command.toOrderProductCreateCommand()));
  }

  /**
   * 주문 상세의 총 금액을 반환한다.
   *
   * <p>단가 × 수량으로 계산되며 해당 책임은 {@link OrderProduct}에 위임된다.
   *
   * @return 주문 상세의 총 금액
   */
  public Price getTotalPrice() {
    return this.orderProduct.getTotalPrice();
  }

  /**
   * 해당 OrderDetail을 특정 Order에 귀속시킨다.
   *
   * <p>양방향 연관관계 설정을 위한 메서드이다.
   *
   * @param order 상위 주문 엔티티
   */
  public void assignTo(Order order) {
    this.order = order;
  }
}
