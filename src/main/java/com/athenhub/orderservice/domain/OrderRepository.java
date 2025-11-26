package com.athenhub.orderservice.domain;

import com.athenhub.orderservice.domain.vo.OrderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 주문(Order) 엔티티에 대한 Repository.
 *
 * <p>{@link Order} Aggregate Root를 대상으로 하는 JPA Repository이며, 주문의 저장 및 조회를 담당한다.
 *
 * <p>식별자는 {@link OrderId} 값 객체를 사용한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, OrderId> {}
