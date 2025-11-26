package com.athenhub.orderservice.domain.exception;

import com.athenhub.commoncore.error.AbstractServiceException;
import com.athenhub.commoncore.error.ErrorCode;

/**
 * 주문 도메인에서 발생하는 예외의 공통 타입이다.
 *
 * <p>Order Aggregate 및 하위 도메인 객체(OrderDetail, OrderProduct 등)에서 비즈니스 규칙 위반 상황을 표현하기 위해 사용된다.
 *
 * <p>에러 코드는 공통 에러 코드({@link ErrorCode})를 기반으로 하며, 필요 시 메시지와 파라미터를 함께 전달할 수 있다.
 *
 * @author 김지원
 * @since 1.0.0
 */
public class OrderDomainException extends AbstractServiceException {

  /**
   * 에러 코드 기반으로 OrderDomainException을 생성한다.
   *
   * @param errorCode 에러 코드
   * @param errorArgs 메시지 포맷에 사용될 인자
   * @author 김지원
   * @since 1.0.0
   */
  public OrderDomainException(ErrorCode errorCode, Object... errorArgs) {
    super(errorCode, errorArgs);
  }

  /**
   * 에러 코드와 커스텀 메시지를 함께 지정하여 OrderDomainException을 생성한다.
   *
   * @param errorCode 에러 코드
   * @param message 커스텀 메시지
   * @param errorArgs 메시지 포맷에 사용될 인자
   * @author 김지원
   * @since 1.0.0
   */
  public OrderDomainException(ErrorCode errorCode, String message, Object... errorArgs) {
    super(errorCode, message, errorArgs);
  }
}
