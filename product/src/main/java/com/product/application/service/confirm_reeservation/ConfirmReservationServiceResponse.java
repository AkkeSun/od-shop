package com.product.application.service.confirm_reeservation;

import lombok.Builder;

@Builder
public record ConfirmReservationServiceResponse(
    Boolean result
) {

    public static ConfirmReservationServiceResponse ofSuccess() {
        return ConfirmReservationServiceResponse.builder()
            .result(true)
            .build();
    }
}
