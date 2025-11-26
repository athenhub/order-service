package com.athenhub.orderservice.application.service;

import com.athenhub.orderservice.application.dto.OrderProductRequest;
import com.athenhub.orderservice.application.service.dto.OrderCreateResponse;
import com.athenhub.orderservice.domain.vo.Orderer;
import java.util.List;

public class OrderCreateService {


  public OrderCreateResponse create(Orderer orderer, List<OrderProductRequest> orderProducts) {
    // 상품   검색
    // Order 생성
    // 상품   추가
    // 주문   생성 완료(재고 차감 이벤트 발행)
  }
}
