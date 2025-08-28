package com.product.application.service.confirm_reeservation;

import com.product.domain.model.Product;
import lombok.Builder;

@Builder
public record ConfirmReservationServiceResponse(
    Boolean result,
    Long productId,
    Long sellerId
) {

    public static ConfirmReservationServiceResponse of(Product product) {
        return ConfirmReservationServiceResponse.builder()
            .result(true)
            .productId(product.getId())
            .sellerId(product.getSellerId())
            .build();
    }
}
