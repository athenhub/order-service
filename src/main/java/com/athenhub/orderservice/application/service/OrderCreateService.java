package com.athenhub.orderservice.application.service;

import com.athenhub.commoncore.error.GlobalErrorCode;
import com.athenhub.orderservice.application.dto.OrderCreatedEvent;
import com.athenhub.orderservice.application.dto.OrderProductRequest;
import com.athenhub.orderservice.application.event.OrderCreatedEventPublisher;
import com.athenhub.orderservice.application.exception.OrderServiceException;
import com.athenhub.orderservice.application.service.dto.OrderCreateResponse;
import com.athenhub.orderservice.application.service.dto.ProductInfo;
import com.athenhub.orderservice.application.service.dto.ShippingInfo;
import com.athenhub.orderservice.domain.Order;
import com.athenhub.orderservice.domain.OrderDetail;
import com.athenhub.orderservice.domain.OrderRepository;
import com.athenhub.orderservice.domain.dto.OrderCreateCommand;
import com.athenhub.orderservice.domain.dto.OrderDetailCreateCommand;
import com.athenhub.orderservice.domain.vo.Orderer;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 주문 생성 유스케이스를 담당하는 애플리케이션 서비스.
 *
 * <p>사용자의 주문 요청을 기반으로 상품 정보를 조회하고, 주문(Order)과 주문 상세(OrderDetail)를 생성한 뒤 저장한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OrderCreateService {

  private final SearchProductService searchProductService;
  private final OrderRepository orderRepository;
  private final OrderCreatedEventPublisher eventPublisher;

  /**
   * 주문을 생성한다.
   *
   * <p>요청된 상품 옵션 기준으로 상품 정보를 조회한 뒤 주문 엔티티와 주문 상세를 생성하고 저장한다.
   *
   * @param orderer 주문자 정보
   * @param orderProducts 주문 상품 요청 목록
   * @param shippingInfo 배송 정보
   * @return 생성된 주문 응답 DTO
   * @author 김지원
   * @since 1.0.0
   */
  public OrderCreateResponse create(
      Orderer orderer, List<OrderProductRequest> orderProducts, ShippingInfo shippingInfo) {
    Map<UUID, OrderProductRequest> orderProductMap = mapByVariantId(orderProducts);

    List<ProductInfo> productInfos = fetchProductInfos(orderProductMap);

    Order order = createOrder(orderer, shippingInfo);
    addOrderDetails(order, productInfos, orderProductMap);

    orderRepository.save(order);

    OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.from(order);
    eventPublisher.publish(orderCreatedEvent);

    return new OrderCreateResponse(order.getId().toUuid());
  }

  /**
   * VariantId 기준으로 주문 상품 요청 목록을 Map으로 변환한다.
   *
   * <p>key 는 {@code productVariantId}, value 는 {@link OrderProductRequest} 이다.
   *
   * @param orderProducts 주문 상품 요청 목록
   * @return VariantId 기준으로 매핑된 Map
   * @author 김지원
   * @since 1.0.0
   */
  private Map<UUID, OrderProductRequest> mapByVariantId(List<OrderProductRequest> orderProducts) {
    return orderProducts.stream()
        .collect(
            Collectors.toMap(
                OrderProductRequest::productVariantId, Function.identity(), (a, b) -> a));
  }

  /**
   * ProductService를 통해 상품 정보를 조회한다.
   *
   * @param orderProductMap 주문 상품 Map (key: variantId)
   * @return 조회된 상품 정보 리스트
   * @author 김지원
   * @since 1.0.0
   */
  private List<ProductInfo> fetchProductInfos(Map<UUID, OrderProductRequest> orderProductMap) {
    return searchProductService.searchProductBy(List.copyOf(orderProductMap.keySet()));
  }

  /**
   * Order 엔티티에 OrderDetail 엔티티들을 추가한다.
   *
   * <p>상품 정보와 주문 수량을 조합하여 주문 상세를 생성하고 Order에 추가한다.
   *
   * @param order 주문 엔티티
   * @param productInfos 상품 정보 리스트
   * @param orderProductMap 주문 상품 Map
   * @author 김지원
   * @since 1.0.0
   */
  private void addOrderDetails(
      Order order, List<ProductInfo> productInfos, Map<UUID, OrderProductRequest> orderProductMap) {
    productInfos.stream()
        .map(info -> toOrderDetailCreateCommand(info, orderProductMap))
        .map(OrderDetail::create)
        .forEach(order::addOrderDetail);
  }

  /**
   * 주문 생성에 필요한 Order 엔티티를 생성한다.
   *
   * @param orderer 주문자 정보
   * @param shippingInfo 배송 정보
   * @return 생성된 Order 엔티티
   * @author 김지원
   * @since 1.0.0
   */
  private Order createOrder(Orderer orderer, ShippingInfo shippingInfo) {
    return Order.create(toOrderCreateCommand(orderer, shippingInfo));
  }

  /**
   * Order 생성에 필요한 커맨드 객체로 변환한다.
   *
   * @param orderer 주문자 정보
   * @param shippingInfo 배송 정보
   * @return OrderCreateCommand
   * @author 김지원
   * @since 1.0.0
   */
  private OrderCreateCommand toOrderCreateCommand(Orderer orderer, ShippingInfo shippingInfo) {
    return OrderCreateCommand.builder()
        .ordererId(orderer.getId())
        .ordererName(orderer.getName())
        .slackId(orderer.getSlackId())
        .receiverId(shippingInfo.receiverId())
        .shippingAddress(shippingInfo.shippingAddress())
        .shippingDetailAddress(shippingInfo.shippingDetailAddress())
        .shippingDueAt(shippingInfo.shippingDueAt())
        .shippingRequestMemo(shippingInfo.shippingRequestMemo())
        .build();
  }

  /**
   * 상품 정보와 주문 수량을 기반으로 OrderDetail 생성 커맨드를 만든다.
   *
   * @param info 상품 정보
   * @param orderProductMap 주문 상품 Map
   * @return OrderDetailCreateCommand
   * @author 김지원
   * @since 1.0.0
   */
  private OrderDetailCreateCommand toOrderDetailCreateCommand(
      ProductInfo info, Map<UUID, OrderProductRequest> orderProductMap) {
    OrderProductRequest request = orderProductMap.get(info.productVariantId());

    if (request == null) {
      throw new OrderServiceException(
          GlobalErrorCode.BAD_REQUEST, "요청에 없는 상품 옵션입니다. variantId=" + info.productVariantId());
    }

    return OrderDetailCreateCommand.builder()
        .producerId(info.vendorId())
        .hubId(info.hubId())
        .productId(info.productId())
        .productName(info.productName())
        .productVariantId(info.productVariantId())
        .productVariantName(info.productVariantName())
        .unitPrice(info.price())
        .quantity(request.quantity())
        .build();
  }
}
