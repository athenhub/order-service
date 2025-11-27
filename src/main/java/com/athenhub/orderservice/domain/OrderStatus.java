package com.athenhub.orderservice.domain;

/**
 * 주문의 상태를 나타내는 Enum이다.
 *
 * <p>주문 라이프사이클에 따라 다음 상태를 가진다:
 *
 * <ul>
 *   <li>{@link #CREATED} : 주문이 생성된 상태
 *   <li>{@link #RESERVED} : 재고 차감이 완료되어 출고를 기다리는 상태
 *   <li>{@link #SHIPPING} : 주문이 출고되어 배송 중인 상태
 *   <li>{@link #COMPLETED} : 배송이 완료된 상태
 *   <li>{@link #CANCELLED} : 주문이 취소된 상태
 * </ul>
 *
 * @author 김지원
 * @since 1.0.0
 */
public enum OrderStatus {

  /** 주문이 생성된 상태(아직 재고 차감 전). */
  CREATED,

  /** 재고 차감이 완료되어 출고를 기다리는 상태. */
  RESERVED,

  /** 주문이 출고되어 배송 중인 상태. */
  SHIPPING,

  /** 주문이 정상적으로 완료된 상태. */
  COMPLETED,

  /** 주문이 취소된 상태. */
  CANCELLED
}
