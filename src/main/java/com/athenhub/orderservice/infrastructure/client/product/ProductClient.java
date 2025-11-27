package com.athenhub.orderservice.infrastructure.client.product;

import com.athenhub.orderservice.application.service.dto.ProductInfo;
import com.athenhub.orderservice.infrastructure.client.config.FeignClientConfig;
import com.athenhub.orderservice.infrastructure.client.product.config.ProductFeignConfig;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 상품 서비스(Product Service)와 통신하기 위한 Feign Client.
 *
 * <p>주문 서비스에서 상품 및 옵션 정보를 조회하기 위해 사용되며, productVariantId 목록을 전달하여 {@link ProductInfo} 목록을 조회한다.
 *
 * @author 김지원
 * @since 1.0.0
 */
@FeignClient(
    name = "product-service",
    path = "/api/v1/products",
    configuration = {FeignClientConfig.class, ProductFeignConfig.class})
public interface ProductClient {

  /**
   * 상품 옵션 ID 목록을 기반으로 상품 정보를 조회한다.
   *
   * <p>여러 개의 {@code productVariantId}를 전달하면 해당 상품의 기본 정보(상품명, 옵션명, 단가, 허브/업체 정보 등)를 반환한다.
   *
   * @param variantIds 조회할 상품 옵션 ID 목록
   * @return {@link ProductInfo} 목록
   * @author 김지원
   * @since 1.0.0
   */
  @GetMapping("/search/variants")
  List<ProductInfo> getProductsBy(@RequestParam List<UUID> variantIds);
}
