package com.athenhub.orderservice.domain.vo;

import com.athenhub.orderservice.domain.dto.OrderProductCreateCommand;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 하나의 주문 상품(OrderProduct)을 나타내는 값 객체.
 *
 * <p>주문에 포함된 단일 상품 및 옵션 정보를 표현하며, 상품 ID, 옵션 ID, 상품명, 옵션명, 단가, 수량을 함께 가진다.
 *
 * <p>{@link OrderDetail} 내부에 포함되어 사용되는 VO(Value Object)이며, 스스로 비즈니스 규칙(수량, 단가)을 검증한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class OrderProduct {

  /** 상품 ID. */
  @Embedded private ProductId productId;

  /** 상품명. */
  private String name;

  /** 옵션명(색상/사이즈 등). */
  private String variantName;

  /** 상품 옵션 ID. */
  @Embedded private ProductVariantId productVariantId;

  /** 단가. */
  @Embedded
  @AttributeOverride(name = "amount", column = @Column(name = "unit_price"))
  private Price unitPrice;

  /** 주문 수량. */
  private int quantity;

  /**
   * OrderProduct를 생성한다.
   *
   * @param productId 상품 ID
   * @param name 상품명
   * @param productVariantId 상품 옵션 ID
   * @param variantName 옵션명
   * @param unitPrice 단가
   * @param quantity 수량
   */
  private OrderProduct(
      ProductId productId,
      String name,
      ProductVariantId productVariantId,
      String variantName,
      Price unitPrice,
      int quantity) {

    validateQuantity(quantity);
    validatePrice(unitPrice);

    this.name = Objects.requireNonNull(name);
    this.variantName = Objects.requireNonNull(variantName);
    this.productId = Objects.requireNonNull(productId);
    this.productVariantId = Objects.requireNonNull(productVariantId);
    this.unitPrice = Objects.requireNonNull(unitPrice);
    this.quantity = quantity;
  }

  /**
   * {@link OrderProductCreateCommand}를 기반으로 OrderProduct를 생성한다.
   *
   * @param command 주문 상품 생성 커맨드
   * @return 생성된 {@link OrderProduct}
   * @author 김지원
   * @since 1.0.0
   */
  public static OrderProduct create(OrderProductCreateCommand command) {
    return new OrderProduct(
        ProductId.of(command.productId()),
        command.name(),
        ProductVariantId.of(command.productVariantId()),
        command.variantName(),
        Price.of(command.unitPrice()),
        command.quantity());
  }

  /**
   * 주문 상품의 총 금액을 계산한다.
   *
   * <p>단가 × 수량으로 계산된다.
   *
   * @return 총 금액
   * @author 김지원
   * @since 1.0.0
   */
  public Price getTotalPrice() {
    return unitPrice.multiply(quantity);
  }

  /**
   * 수량의 유효성을 검증한다.
   *
   * <p>수량은 반드시 1 이상이어야 한다.
   *
   * @param quantity 주문 수량
   * @author 김지원
   * @since 1.0.0
   */
  private static void validateQuantity(int quantity) {
    if (quantity <= 0) {
      throw new IllegalStateException("주문 수량은 1 이상이어야 합니다.");
    }
  }

  /**
   * 단가의 유효성을 검증한다.
   *
   * <p>단가는 반드시 0보다 커야 한다.
   *
   * @param price 단가
   * @author 김지원
   * @since 1.0.0
   */
  private static void validatePrice(Price price) {
    if (price.isZeroOrNegative()) {
      throw new IllegalStateException("단가는 0보다 커야 합니다.");
    }
  }
}
