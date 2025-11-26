package com.athenhub.orderservice.infrastructure.service;

import com.athenhub.orderservice.application.service.SearchProductService;
import com.athenhub.orderservice.application.service.dto.ProductInfo;
import com.athenhub.orderservice.infrastructure.client.product.ProductClient;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 상품 조회를 위한 인프라 계층 구현체.
 *
 * <p>{@link SearchProductService}를 구현하며, 실제 상품 조회는 {@link ProductClient}(Feign Client)를 통해 Product
 * Service에 위임한다.
 *
 * <p>주문 서비스에서는 해당 클래스를 통해 상품/옵션 정보 (상품명, 옵션명, 가격, 허브, 벤더 등)를 조회한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
public class ProductSearchClient implements SearchProductService {

  private final ProductClient productClient;

  /**
   * 상품 옵션 ID 목록을 기반으로 상품 정보를 조회한다.
   *
   * <p>내부적으로 {@link ProductClient#getProductsBy(List)}를 호출하여 Product Service로부터 {@link ProductInfo}
   * 목록을 전달받는다.
   *
   * @param variantIds 조회할 상품 옵션 ID 목록
   * @return 조회된 상품 정보 목록
   * @author 김지원
   * @since 1.0.0
   */
  @Override
  public List<ProductInfo> searchProductBy(List<UUID> variantIds) {
    return productClient.getProductsBy(variantIds);
  }
}
