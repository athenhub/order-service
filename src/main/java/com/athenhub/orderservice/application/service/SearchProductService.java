package com.athenhub.orderservice.application.service;

import com.athenhub.orderservice.application.service.dto.ProductInfo;
import java.util.List;
import java.util.UUID;

/**
 * 상품 옵션 정보를 조회하기 위한 인터페이스.
 *
 * <p>주문 서비스에서 상품 서비스와의 의존성을 제거하기 위해 추상화된 포트(Port) 역할을 하며, 구현체는 인프라 계층에서 제공된다.
 *
 * <p>구현체 예시: {@code ProductSearchClient}
 *
 * @author 김지원
 * @since 1.0.0
 */
public interface SearchProductService {

  /**
   * 상품 옵션 ID 목록을 기준으로 상품 정보를 조회한다.
   *
   * <p>전달된 {@code variantIds}에 해당하는 상품 및 옵션 정보를 {@link ProductInfo} 목록으로 반환한다.
   *
   * @param variantIds 조회할 상품 옵션 ID 목록
   * @return 조회된 상품 정보 목록
   * @author 김지원
   * @since 1.0.0
   */
  List<ProductInfo> searchProductBy(List<UUID> variantIds);
}
