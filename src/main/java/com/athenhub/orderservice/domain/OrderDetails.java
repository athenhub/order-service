package com.athenhub.orderservice.domain;

import com.athenhub.commoncore.error.GlobalErrorCode;
import com.athenhub.orderservice.domain.exception.OrderDomainException;
import com.athenhub.orderservice.domain.vo.OrderDetailId;
import com.athenhub.orderservice.domain.vo.Price;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * 주문 상세 목록을 관리하는 일급 컬렉션(OrderDetails)이다.
 *
 * <p>Order Aggregate 내부에서만 사용되며, {@link OrderDetail} 리스트에 대한 추가, 조회, 총 금액 계산 등의 책임을 가진다.
 *
 * <p>외부에서 컬렉션 자체를 직접 조작하지 못하도록 불변 리스트를 반환한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@Embeddable
public class OrderDetails {

  /** 주문에 포함된 상품 목록. */
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderDetail> values = new ArrayList<>();

  /**
   * 주문 전체 금액을 계산한다.
   *
   * <p>각 주문 상세의 총 금액을 합산하여 반환한다.
   *
   * @return 주문 총 금액
   */
  public Price getTotalPrice() {
    return values.stream().map(OrderDetail::getTotalPrice).reduce(Price::add).orElse(Price.ZERO);
  }

  /**
   * 주문 상세를 추가하고 연관관계를 설정한다.
   *
   * <p>OrderDetail과 Order 간의 연관관계를 함께 지정한다.
   *
   * @param orderDetail 추가할 주문 상세
   * @param order 상위 주문 엔티티
   */
  public void add(OrderDetail orderDetail, Order order) {
    values.add(orderDetail);
    orderDetail.assignTo(order);
  }

  /**
   * 주문 상세를 식별자로 조회한다.
   *
   * @param targetId 조회할 주문 상세 ID
   * @return 조회된 주문 상세
   * @throws OrderDomainException 존재하지 않을 경우
   */
  public OrderDetail get(OrderDetailId targetId) {
    return values.stream()
        .filter(v -> v.getId().equals(targetId))
        .findFirst()
        .orElseThrow(
            () -> new OrderDomainException(GlobalErrorCode.NOT_FOUND, "요청하신 상품을 찾을 수 없습니다."));
  }

  /**
   * 주문 상세 목록을 불변 리스트로 반환한다.
   *
   * @return 불변 주문 상세 리스트
   */
  public List<OrderDetail> toList() {
    return List.copyOf(values);
  }
}
