package com.athenhub.orderservice.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.athenhub.orderservice.application.dto.OrderProductRequest;
import com.athenhub.orderservice.application.event.OrderCreatedEventPublisher;
import com.athenhub.orderservice.application.service.dto.OrderCreateResponse;
import com.athenhub.orderservice.application.service.dto.ProductInfo;
import com.athenhub.orderservice.application.service.dto.ShippingInfo;
import com.athenhub.orderservice.domain.Order;
import com.athenhub.orderservice.domain.OrderDetail;
import com.athenhub.orderservice.domain.OrderRepository;
import com.athenhub.orderservice.domain.vo.OrderId;
import com.athenhub.orderservice.domain.vo.Orderer;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link OrderCreateService} 통합 테스트.
 *
 * <p>주문 생성 시 Order와 OrderDetail이 정상적으로 생성되는지 검증한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@SpringBootTest
@Transactional
class OrderCreateServiceTest {

  @Autowired private OrderCreateService orderCreateService;

  @Autowired private OrderRepository orderRepository;

  @MockitoBean private SearchProductService searchProductService;

  @MockitoBean private OrderCreatedEventPublisher eventPublisher;

  @Test
  @DisplayName("주문을 생성하고 OrderDetail이 정상적으로 추가된다.")
  void createOrder_success() {
    // given
    UUID productId = UUID.randomUUID();
    UUID variantId1 = UUID.randomUUID();
    UUID variantId2 = UUID.randomUUID();

    List<OrderProductRequest> orderProducts =
        List.of(
            new OrderProductRequest(productId, variantId1, 2),
            new OrderProductRequest(productId, variantId2, 1));

    mockSearchProductService(productId, variantId1, variantId2);

    Orderer orderer = Orderer.of(UUID.randomUUID(), "테스터", "slack-id");

    ShippingInfo shippingInfo =
        new ShippingInfo(
            UUID.randomUUID(),
            "서울시 강남구",
            "101동 202호",
            LocalDateTime.now().plusDays(1),
            "문 앞에 놔주세요");

    // when
    OrderCreateResponse response = orderCreateService.create(orderer, orderProducts, shippingInfo);

    // then
    Order savedOrder = orderRepository.findById(OrderId.of(response.orderId())).orElseThrow();

    assertThat(savedOrder).isNotNull();
    assertThat(savedOrder.getTotalPrice().value()).isEqualTo(40_000L);

    List<OrderDetail> orderDetails = savedOrder.getOrderDetails().toList();

    assertThat(orderDetails)
        .hasSize(2)
        .extracting("orderProduct.unitPrice.amount", "orderProduct.quantity")
        .containsExactlyInAnyOrder(tuple(10_000L, 2), tuple(20_000L, 1));

    // 이벤트 발행 확인
    verify(eventPublisher, times(1)).publish(any());
  }

  /**
   * SearchProductService를 Mocking한다.
   *
   * @param productId 상품 ID
   * @param variantId1 옵션 ID 1
   * @param variantId2 옵션 ID 2
   * @author 김지원
   * @since 1.0.0
   */
  private void mockSearchProductService(UUID productId, UUID variantId1, UUID variantId2) {
    List<ProductInfo> productInfos =
        List.of(
            new ProductInfo(
                productId,
                "테스트 상품",
                "RED / 265",
                variantId1,
                10_000L,
                UUID.randomUUID(),
                UUID.randomUUID()),
            new ProductInfo(
                productId,
                "테스트 상품",
                "RED / 270",
                variantId2,
                20_000L,
                UUID.randomUUID(),
                UUID.randomUUID()));

    when(searchProductService.searchProductBy(anyList())).thenReturn(productInfos);
  }
}
