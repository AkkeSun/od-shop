package com.product.application.service.confirm_reeservation;

import com.product.domain.model.Product;
import com.product.domain.model.ProductReserveHistory;
import lombok.Builder;

@Builder
public record ConfirmReservationServiceResponse(
    Boolean result,
    Long productId,
    Long sellerId,
    long buyQuantity
) {

    public static ConfirmReservationServiceResponse of(Product product,
        ProductReserveHistory reserveHistory) {
        return ConfirmReservationServiceResponse.builder()
            .result(true)
            .productId(product.getId())
            .sellerId(product.getSellerId())
            .buyQuantity(reserveHistory.reservedQuantity())
            .build();
    }
}
