package com.athenhub.orderservice.application.dto;

import java.util.UUID;

public record OrderProductRequest(
    UUID productId,
    UUID productVariantId,
    int quantity
) {
}
