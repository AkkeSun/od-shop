package com.product.application.service.create_reservation;

import com.product.domain.model.ProductReserveHistory;
import lombok.Builder;

@Builder
public record CreateReservationServiceResponse(
    Long reserveId
) {

    public static CreateReservationServiceResponse of(ProductReserveHistory history) {
        return CreateReservationServiceResponse.builder()
            .reserveId(history.id())
            .build();
    }
}
