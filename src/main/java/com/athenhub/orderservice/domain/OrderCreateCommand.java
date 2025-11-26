package com.athenhub.orderservice.domain;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 주문 생성을 위한 커맨드 객체이다.
 *
 * <p>주문 생성 시 필요한 기본 정보를 담아 Application → Domain 계층으로 전달한다. Order Aggregate를 생성하는 데 필요한 최소한의 식별 정보와
 * 배송 정보를 포함한다.
 *
 * <p><b>필수 값</b>:
 *
 * <ul>
 *   <li>{@code receiverId} : 수령(업체) 식별자
 *   <li>{@code ordererId} : 주문자 식별자
 *   <li>{@code ordererName} : 주문자 이름
 *   <li>{@code slackId} : 주문자 Slack ID
 * </ul>
 *
 * <p><b>선택 값</b>:
 *
 * <ul>
 *   <li>{@code shippingAddress} : 배송지 주소
 *   <li>{@code shippingDetailAddress} : 배송지 상세 주소
 *   <li>{@code shippingDueAt} : 납품 희망 일시
 *   <li>{@code shippingRequestMemo} : 배송 요청 메모
 * </ul>
 *
 * @author 김지원
 * @since 1.0.0
 */
public record OrderCreateCommand(
    @NotNull UUID receiverId,
    @NotNull UUID ordererId,
    @NotNull String ordererName,
    @NotNull String slackId,
    String shippingAddress,
    String shippingDetailAddress,
    LocalDateTime shippingDueAt,
    String shippingRequestMemo) {}
