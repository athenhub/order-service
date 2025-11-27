package com.athenhub.orderservice.presentation;

import com.athenhub.commonmvc.security.AuthenticatedUser;
import com.athenhub.orderservice.application.service.OrderCreateService;
import com.athenhub.orderservice.application.service.dto.OrderCreateResponse;
import com.athenhub.orderservice.application.service.dto.ShippingInfo;
import com.athenhub.orderservice.domain.vo.Orderer;
import com.athenhub.orderservice.presentation.dto.CreateOrderRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 주문 생성 요청을 처리하는 컨트롤러.
 *
 * <p>클라이언트로부터 주문 상품 정보와 배송 정보를 전달받아 {@link OrderCreateService} 를 통해 주문을 생성한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderCreateController {

  private final OrderCreateService orderCreateService;

  /**
   * 주문을 생성한다.
   *
   * @param request 주문 생성 요청 정보
   * @return 생성된 주문 ID
   * @author 김지원
   * @since 1.0.0
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OrderCreateResponse createOrder(
      @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
      @RequestBody @Valid CreateOrderRequest request) {
    Orderer orderer =
        Orderer.of(authenticatedUser.id(), authenticatedUser.name(), authenticatedUser.slackId());

    ShippingInfo shippingInfo =
        new ShippingInfo(
            request.receiverId(),
            request.shippingAddress(),
            request.shippingDetailAddress(),
            request.shippingDueAt(),
            request.shippingRequestMemo());

    return orderCreateService.create(orderer, request.orderProducts(), shippingInfo);
  }
}
