package com.athenhub.orderservice.application.exception;

import com.athenhub.commoncore.error.AbstractServiceException;
import com.athenhub.commoncore.error.ErrorCode;

/**
 * 주문 서비스 계층에서 발생하는 예외의 공통 타입이다.
 *
 * <p>주문 생성, 조회, 변경 등 애플리케이션 레벨에서 발생하는 비즈니스 예외를 표현하기 위한 전용 예외 클래스이다.
 *
 * <p>에러 코드는 공통 에러 코드({@link ErrorCode})를 기반으로 하며, 필요 시 커스텀 메시지와 메시지 파라미터를 함께 전달할 수 있다.
 *
 * @author 김지원
 * @since 1.0.0
 */
public class OrderServiceException extends AbstractServiceException {

  /**
   * 에러 코드 기반으로 OrderServiceException을 생성한다.
   *
   * @param errorCode 에러 코드
   * @param errorArgs 메시지 포맷에 사용될 인자
   * @author 김지원
   * @since 1.0.0
   */
  public OrderServiceException(ErrorCode errorCode, Object... errorArgs) {
    super(errorCode, errorArgs);
  }

  /**
   * 에러 코드와 커스텀 메시지를 함께 지정하여 OrderServiceException을 생성한다.
   *
   * @param errorCode 에러 코드
   * @param message 커스텀 메시지
   * @param errorArgs 메시지 포맷에 사용될 인자
   * @author 김지원
   * @since 1.0.0
   */
  public OrderServiceException(ErrorCode errorCode, String message, Object... errorArgs) {
    super(errorCode, message, errorArgs);
  }
}
