package com.athenhub.orderservice.global.event;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 도메인 이벤트 발행 환경을 초기화하는 설정 클래스이다.
 *
 * <p>애플리케이션이 시작될 때 {@link Events}에 {@link ApplicationContext}를 주입하여 정적 Event Publisher가 Spring
 * 컨텍스트를 통해 이벤트를 발행할 수 있도록 구성한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@EnableAsync
@Configuration
public class EventConfig {

  /** Spring 컨테이너의 ApplicationContext. */
  @Autowired private ApplicationContext ctx;

  /**
   * Events 클래스에 ApplicationContext를 등록하는 초기화 빈이다.
   *
   * <p>Spring Bean 초기화 이후 실행되며, {@link Events#setPublisher(ApplicationContext)}를 호출해 이벤트 발행자를 설정한다.
   *
   * @return Event Publisher 초기화를 수행하는 InitializingBean
   * @author 김지원
   * @since 1.0.0
   */
  @Bean
  public InitializingBean eventsInitializer() {
    return () -> Events.setPublisher(ctx);
  }
}
