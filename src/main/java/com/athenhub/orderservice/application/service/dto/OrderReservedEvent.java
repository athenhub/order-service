package com.athenhub.orderservice.application.service.dto;

import com.athenhub.orderservice.domain.vo.OrderId;

/**
 * 주문이 예약 상태로 변경되었음을 알리는 도메인 이벤트이다.
 *
 * @author 김지원
 * @since 1.0.0
 */
public record OrderReservedEvent(OrderId orderId) {}
