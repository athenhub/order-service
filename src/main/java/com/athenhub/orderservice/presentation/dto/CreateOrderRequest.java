package com.athenhub.orderservice.presentation.dto;

import com.athenhub.orderservice.application.dto.OrderProductRequest;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 주문 생성 요청 DTO.
 *
 * @param receiverId 수령자 ID
 * @param shippingAddress 배송 주소
 * @param shippingDetailAddress 배송 상세 주소
 * @param shippingDueAt 배송 요청 일시
 * @param shippingRequestMemo 배송 요청 메모
 * @param orderProducts 주문 상품 목록
 * @author 김지원
 * @since 1.0.0
 */
public record CreateOrderRequest(
    @NotNull UUID receiverId,
    String shippingAddress,
    String shippingDetailAddress,
    LocalDateTime shippingDueAt,
    String shippingRequestMemo,
    @NotNull List<OrderProductRequest> orderProducts) {}
