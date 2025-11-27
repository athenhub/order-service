package com.athenhub.orderservice.application.service.dto;

import java.util.UUID;

/**
 * 주문 생성 응답 DTO.
 *
 * <p>주문 생성 완료 후, 생성된 주문의 식별자를 반환한다.
 *
 * @param orderId 생성된 주문의 고유 식별자
 * @author 김지원
 * @since 1.0.0
 */
public record OrderCreateResponse(UUID orderId) {}
