package com.athenhub.orderservice.domain.dto;

import java.util.UUID;

/**
 * 주문 상품 생성을 위한 커맨드 객체이다.
 *
 * <p>주문 상세(OrderDetail) 내 개별 상품 정보를 전달하는 DTO로, 상품 식별자, 옵션 식별자, 단가, 수량 정보를 포함한다.
 *
 * <p>Domain 계층의 {@code OrderProduct}를 생성하기 위한 입력 데이터 역할을 한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
public record OrderProductCreateCommand(
    /* 상품 식별자. */
    UUID productId,

    /* 상품 옵션 식별자. */
    UUID productVariantId,

    /* 상품 단가(개당 가격). */
    long unitPrice,

    /* 주문 수량. */
    int quantity) {}
