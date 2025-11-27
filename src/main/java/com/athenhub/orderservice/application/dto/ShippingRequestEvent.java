package com.athenhub.orderservice.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

/**
 * 배송 요청을 위해 발행되는 이벤트 DTO이다.
 *
 * <p>주문이 예약/출고 단계로 전환될 때, 배송 서비스로 전달할 최소한의 배송 정보를 담는다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@Builder
public record ShippingRequestEvent(
    UUID orderId,
    UUID sourceHubId,
    String streetAddress,
    String detailAddress,
    UUID receiverId,
    String receiverName,
    String receiverSlackId,
    LocalDateTime shippingDueAt,
    String shippingRequestMemo) {}
