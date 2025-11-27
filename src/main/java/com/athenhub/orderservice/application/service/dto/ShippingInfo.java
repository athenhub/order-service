package com.athenhub.orderservice.application.service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 배송 정보를 담는 DTO.
 *
 * <p>주문 생성 시 필요한 수령자 정보와 배송지, 배송 요청 사항을 포함한다.
 *
 * @param receiverId 수령자 ID
 * @param shippingAddress 배송 주소
 * @param shippingDetailAddress 배송 상세 주소
 * @param shippingDueAt 배송 희망 일시
 * @param shippingRequestMemo 배송 요청 메모
 * @author 김지원
 * @since 1.0.0
 */
public record ShippingInfo(
    UUID receiverId,
    String shippingAddress,
    String shippingDetailAddress,
    LocalDateTime shippingDueAt,
    String shippingRequestMemo) {}
