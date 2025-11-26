package com.athenhub.orderservice.domain.dto;

import com.athenhub.orderservice.domain.Order;
import com.athenhub.orderservice.domain.vo.OrderProduct;
import java.util.UUID;

/**
 * 주문 상세 생성을 위한 커맨드 객체이다.
 *
 * <p>OrderDetail 도메인을 생성하는 데 필요한 모든 정보를 담는다. 외부 계층(Application/Presentation)에서 전달받아 Domain 계층으로
 * 변환하는 역할을 한다.
 *
 * <p>내부적으로 {@link #toOrderProductCreateCommand()}를 통해 {@link OrderProductCreateCommand}로 변환되어
 * {@link OrderProduct} 생성에 활용된다.
 *
 * @author 김지원
 * @since 1.0.0
 */
public record OrderDetailCreateCommand(
    /* 주문 상세 식별자. (일반적으로 Domain에서 새로 생성되므로 필수는 아님) */
    UUID id,

    /* 상품 공급자(업체) 식별자. */
    UUID producerId,

    /* 허브 식별자. */
    UUID hubId,

    /* 주문 상품 도메인 객체 (직접 전달되는 경우). */
    OrderProduct orderProduct,

    /* 상위 주문 엔티티. */
    Order order,

    /* 상품 식별자. */
    UUID productId,

    /* 상품 옵션 식별자. */
    UUID productVariantId,

    /* 상품 단가(개당 가격). */
    long unitPrice,

    /* 주문 수량. */
    int quantity) {

  /**
   * OrderProduct 생성을 위한 커맨드로 변환한다.
   *
   * <p>상품 ID, 옵션 ID, 단가, 수량 정보를 기반으로 {@link OrderProductCreateCommand}를 생성한다.
   *
   * @return OrderProductCreateCommand
   * @author 김지원
   * @since 1.0.0
   */
  public OrderProductCreateCommand toOrderProductCreateCommand() {
    return new OrderProductCreateCommand(productId, productVariantId, unitPrice, quantity);
  }
}
