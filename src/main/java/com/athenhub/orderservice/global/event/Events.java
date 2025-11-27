package com.athenhub.orderservice.global.event;

import org.springframework.context.ApplicationEventPublisher;

/**
 * 도메인 이벤트를 발행하기 위한 전역 유틸리티 클래스이다.
 *
 * <p>Spring {@link ApplicationEventPublisher}를 정적으로 보관하고, 어디서든 도메인 이벤트를 발행할 수 있도록 지원한다.
 *
 * <p>실제 Publisher는 {@link EventConfig}에서 애플리케이션 시작 시 설정된다.
 *
 * @author 김지원
 * @since 1.0.0
 */
public class Events {

  /** Spring 이벤트 발행자. */
  private static ApplicationEventPublisher publisher;

  /**
   * ApplicationEventPublisher를 설정한다.
   *
   * <p>애플리케이션 초기화 시 {@link EventConfig}에서 호출되며, 정적 Publisher를 등록하는 역할을 한다.
   *
   * @param publisher Spring ApplicationEventPublisher
   * @author 김지원
   * @since 1.0.0
   */
  static void setPublisher(ApplicationEventPublisher publisher) {
    Events.publisher = publisher;
  }

  /**
   * 도메인 이벤트를 발행한다.
   *
   * <p>Publisher가 초기화되지 않은 경우에는 아무 동작도 하지 않는다.
   *
   * @param event 발행할 이벤트 객체
   * @author 김지원
   * @since 1.0.0
   */
  public static void trigger(Object event) {
    if (publisher == null) {
      return;
    }
    publisher.publishEvent(event);
  }
}
