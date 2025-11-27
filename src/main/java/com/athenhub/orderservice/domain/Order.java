package com.athenhub.orderservice.domain;

import com.athenhub.orderservice.domain.dto.OrderCreateCommand;
import com.athenhub.orderservice.domain.vo.OrderDetailId;
import com.athenhub.orderservice.domain.vo.OrderId;
import com.athenhub.orderservice.domain.vo.Orderer;
import com.athenhub.orderservice.domain.vo.Price;
import com.athenhub.orderservice.domain.vo.ShippingAddress;
import com.athenhub.orderservice.domain.vo.ShippingId;
import com.athenhub.orderservice.domain.vo.VendorId;
import com.athenhub.orderservice.global.domain.AbstractAuditEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 주문(Order) Aggregate Root.
 *
 * <p>주문의 생성, 배송, 완료, 취소 등 전반적인 상태 변경을 관리하며 주문 상세({@link OrderDetails})를 통해 총 금액을 계산한다.
 *
 * <p>주문에 포함된 상품(OrderDetail)은 생성 이후 수정/삭제하지 않으며, 변경이 필요할 경우 취소 후 재주문 방식으로 처리한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@Entity
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Order extends AbstractAuditEntity {

  /** 주문 식별자. */
  @EmbeddedId private OrderId id;

  /** 주문자 정보. */
  @Embedded private Orderer orderer;

  /** 수령 업체(구매 대상) 식별자. */
  @Embedded private VendorId receiverId;

  /** 주문 상태. */
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  /** 주문 총 금액. */
  @Embedded
  @AttributeOverride(name = "amount", column = @Column(name = "total_price"))
  private Price totalPrice;

  /** 배송지 정보. */
  @Embedded private ShippingAddress shippingAddress;

  /** 납품(배송 완료) 기한. */
  private LocalDateTime shippingDueAt;

  /** 배송 요청 사항(메모). */
  private String shippingRequestMemo;

  /** 배송(출고) 식별자. */
  @Embedded private ShippingId shippingId;

  /** 배송 시작 시각. */
  private LocalDateTime shippedAt;

  /** 주문 완료 시각. */
  private LocalDateTime completedAt;

  /** 주문 취소 시각. */
  private LocalDateTime canceledAt;

  /** 주문 취소 이유. */
  private String cancelReason;

  /** 주문 상세(상품 목록). */
  @Embedded private OrderDetails orderDetails = new OrderDetails();

  /** 재고가 차감되어 배송을 기다리는 상태 */
  private LocalDateTime reservedAt;

  /**
   * 주문 상세를 기반으로 총 금액을 재계산한다.
   *
   * @return 재계산된 총 금액
   */
  private Price calculateTotalPrice() {
    this.totalPrice = orderDetails.getTotalPrice();
    return totalPrice;
  }

  /**
   * Order 생성자.
   *
   * <p>외부에서는 {@link #create(OrderCreateCommand)}를 통해서만 생성된다.
   *
   * @param id 주문 ID
   * @param orderer 주문자 정보
   * @param shippingDueAt 납품 기한
   * @param shippingRequestMemo 배송 요청 메모
   * @param status 초기 주문 상태
   * @param totalPrice 초기 총 금액
   * @param shippingAddress 배송지
   * @param shippingId 배송 식별자
   * @param shippedAt 배송 시작 시각
   */
  private Order(
      OrderId id,
      Orderer orderer,
      LocalDateTime shippingDueAt,
      String shippingRequestMemo,
      OrderStatus status,
      Price totalPrice,
      ShippingAddress shippingAddress,
      ShippingId shippingId,
      LocalDateTime shippedAt) {

    this.id = Objects.requireNonNull(id);
    this.orderer = Objects.requireNonNull(orderer);
    this.shippingDueAt = shippingDueAt;
    this.shippingRequestMemo = shippingRequestMemo;
    this.status = Objects.requireNonNull(status);
    this.totalPrice = Objects.requireNonNull(totalPrice);
    this.shippingAddress = shippingAddress;
    this.shippingId = shippingId;
    this.shippedAt = shippedAt;
  }

  /**
   * 주문을 생성한다.
   *
   * <p>초기 상태는 {@link OrderStatus#CREATED}이며, 총 금액은 {@link Price#ZERO}로 초기화된다.
   *
   * @param orderCreateCommand 주문 생성 커맨드
   * @return 생성된 주문
   */
  public static Order create(OrderCreateCommand orderCreateCommand) {

    Objects.requireNonNull(orderCreateCommand, "OrderCreateCommand 는 null일 수 없습니다.");

    validateShippingDueAt(orderCreateCommand.shippingDueAt());

    return new Order(
        OrderId.create(),
        Orderer.of(
            orderCreateCommand.ordererId(),
            orderCreateCommand.ordererName(),
            orderCreateCommand.slackId()),
        orderCreateCommand.shippingDueAt(),
        orderCreateCommand.shippingRequestMemo(),
        OrderStatus.CREATED,
        Price.ZERO,
        ShippingAddress.of(
            orderCreateCommand.shippingAddress(), orderCreateCommand.shippingDetailAddress()),
        null, // shippingId (출고 전)
        null // shippedAt (출고 전)
        );
  }

  /**
   * 주문을 배송 중 상태로 변경한다.
   *
   * @param shippingId 배송 식별자
   * @param shippedAt 배송 시작 시각
   */
  public void shipping(UUID shippingId, LocalDateTime shippedAt) {

    validateCanChangeStatus();

    this.shippingId = ShippingId.of(shippingId);
    this.status = OrderStatus.SHIPPING;
    this.shippedAt = shippedAt;
  }

  /**
   * 주문을 완료 상태로 변경한다.
   *
   * @param completedAt 주문 완료 시각
   */
  public void completed(LocalDateTime completedAt) {

    validateCanChangeStatus();

    this.status = OrderStatus.COMPLETED;
    this.completedAt = completedAt;
  }

  /**
   * 주문을 취소 상태로 변경한다.
   *
   * @param canceledAt 주문 취소 시각
   */
  public void cancel(String cancelReason, LocalDateTime canceledAt) {

    validateCanChangeStatus();

    this.status = OrderStatus.CANCELLED;
    this.canceledAt = canceledAt;
    this.cancelReason = cancelReason;
  }

  public void reserved(LocalDateTime reservedAt) {

    validateCanChangeStatus();

    this.status = OrderStatus.RESERVED;
    this.reservedAt = reservedAt;
  }

  /**
   * 주문 상세를 추가하고 총 금액을 갱신한다.
   *
   * @param orderDetail 추가할 주문 상세
   */
  public void addOrderDetail(OrderDetail orderDetail) {

    Objects.requireNonNull(orderDetail, "OrderDetail 은 null일 수 없습니다.");

    orderDetails.add(orderDetail, this);
    calculateTotalPrice();
  }

  /**
   * 주문 상세를 ID로 조회한다.
   *
   * @param orderDetailId 주문 상세 ID
   * @return 주문 상세
   */
  public OrderDetail getOrderDetail(OrderDetailId orderDetailId) {
    return orderDetails.get(orderDetailId);
  }

  /**
   * 배송 기한이 현재 이후인지 검증한다.
   *
   * @param shippingDueAt 배송 기한
   */
  private static void validateShippingDueAt(LocalDateTime shippingDueAt) {
    if (shippingDueAt != null && shippingDueAt.isBefore(LocalDateTime.now())) {
      throw new IllegalStateException("배송 기한은 현재 이후여야 합니다.");
    }
  }

  /**
   * 현재 주문 상태가 변경 가능한지 검증한다.
   *
   * <p>이미 COMPLETED 또는 CANCEL 상태인 경우 상태 변경이 불가능하다.
   */
  private void validateCanChangeStatus() {
    if (this.status == OrderStatus.CANCELLED || this.status == OrderStatus.COMPLETED) {
      throw new IllegalStateException("이미 완료되었거나 취소된 주문은 상태를 변경할 수 없습니다.");
    }
  }
}
