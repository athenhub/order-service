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

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class OrderProduct {

  @Embedded private ProductId productId;

  @Embedded private ProductVariantId productVariantId;

  @Embedded
  @AttributeOverride(name = "amount", column = @Column(name = "unit_price"))
  private Price unitPrice;

  private int quantity;

  private OrderProduct(
      ProductId productId, ProductVariantId productVariantId, Price unitPrice, int quantity) {

    validateQuantity(quantity);
    validatePrice(unitPrice);

    this.productId = Objects.requireNonNull(productId);
    this.productVariantId = Objects.requireNonNull(productVariantId);
    this.unitPrice = Objects.requireNonNull(unitPrice);
    this.quantity = quantity;
  }

  public static OrderProduct create(OrderProductCreateCommand command) {
    return new OrderProduct(
        ProductId.of(command.productId()),
        ProductVariantId.of(command.productVariantId()),
        Price.of(command.unitPrice()),
        command.quantity());
  }

  public Price getTotalPrice() {
    return unitPrice.multiply(quantity);
  }

  private static void validateQuantity(int quantity) {
    if (quantity <= 0) {
      throw new IllegalStateException("주문 수량은 1 이상이어야 합니다.");
    }
  }

  private static void validatePrice(Price price) {
    if (price.isZeroOrNegative()) {
      throw new IllegalStateException("단가는 0보다 커야 합니다.");
    }
  }
}
