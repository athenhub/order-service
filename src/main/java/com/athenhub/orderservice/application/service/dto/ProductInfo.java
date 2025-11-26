package com.athenhub.orderservice.application.service.dto;

import java.util.UUID;

/**
 * 상품 조회 결과를 담는 DTO이다.
 *
 * <p>주문 생성을 위해 상품 서비스(Product Service)로부터 전달받는 정보이며, 상품 및 상품 옵션에 대한 핵심 정보와 가격, 소속 정보를 포함한다.
 *
 * @param productId 상품 식별자
 * @param productName 상품명
 * @param productVariantName 상품 옵션명(색상/사이즈 등)
 * @param productVariantId 상품 옵션 식별자
 * @param price 단가(개당 가격)
 * @param hubId 상품이 속한 허브 ID
 * @param vendorId 상품을 제공하는 업체(벤더) ID
 * @author 김지원
 * @since 1.0.0
 */
public record ProductInfo(
    UUID productId,
    String productName,
    String productVariantName,
    UUID productVariantId,
    long price,
    UUID hubId,
    UUID vendorId) {}
