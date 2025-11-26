package com.athenhub.orderservice.application.service;

import com.athenhub.orderservice.application.service.dto.ProductInfo;
import com.athenhub.orderservice.domain.vo.ProductId;
import com.athenhub.orderservice.domain.vo.ProductVariantId;
import java.util.List;

public interface SearchProductService {

  List<ProductInfo> searchBy(ProductId productId, ProductVariantId variantId);

}
