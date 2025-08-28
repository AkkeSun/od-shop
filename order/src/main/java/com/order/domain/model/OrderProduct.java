package com.order.domain.model;

import grpc.product.ConfirmProductReservationResponse;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record OrderProduct(
    Long id,
    Long orderNumber,
    Long productId,
    Long sellerId,
    LocalDateTime regDateTime
) {

    public static OrderProduct of(ConfirmProductReservationResponse serviceResponse) {
        return OrderProduct.builder()
            .productId(serviceResponse.getProductId())
            .sellerId(serviceResponse.getSellerId())
            .build();
    }
}
