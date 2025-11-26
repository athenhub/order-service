package com.athenhub.orderservice.application.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

/**
 * 주문 요청 시 개별 상품 정보를 전달하는 DTO.
 *
 * <p>클라이언트가 주문을 생성할 때 사용하는 입력 데이터로, 상품 식별자와 옵션 식별자, 주문 수량을 포함한다.
 *
 * <p>애플리케이션 계층에서 {@code OrderCreateService}를 통해 도메인 객체({@code OrderProduct}) 생성에 사용된다.
 *
 * @param productId 상품 식별자
 * @param productVariantId 상품 옵션 식별자
 * @param quantity 주문 수량
 * @author 김지원
 * @since 1.0.0
 */
public record OrderProductRequest(
    @NotNull UUID productId, @NotNull UUID productVariantId, @NotNull int quantity) {}
