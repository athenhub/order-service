package com.athenhub.orderservice.domain;

/**
 * 주문의 상태를 나타내는 Enum이다.
 *
 * <p>주문 라이프사이클에 따라 다음 상태를 가진다:
 *
 * <ul>
 *   <li>{@link #CREATED} : 주문이 생성된 상태
 *   <li>{@link #SHIPPING} : 주문이 출고되어 배송 중인 상태
 *   <li>{@link #COMPLETED} : 배송이 완료된 상태
 *   <li>{@link #CANCEL} : 주문이 취소된 상태
 * </ul>
 *
 * @author 김지원
 * @since 1.0.0
 */
public enum OrderStatus {

  /** 주문이 최초 생성된 상태. */
  CREATED,

  /** 주문이 출고되어 배송 중인 상태. */
  SHIPPING,

  /** 주문이 정상적으로 완료된 상태. */
  COMPLETED,

  /** 주문이 취소된 상태. */
  CANCEL
}
