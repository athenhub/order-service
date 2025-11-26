package com.athenhub.orderservice.application.service.dto;

import java.util.UUID;

public record OrderCreateResponse(
    UUID orderId
) {

}
