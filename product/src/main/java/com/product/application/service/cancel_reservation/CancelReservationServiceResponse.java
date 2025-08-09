package com.product.application.service.cancel_reservation;

import lombok.Builder;

@Builder
public record CancelReservationServiceResponse(
    boolean result
) {
    public static CancelReservationServiceResponse ofSuccess() {
        return CancelReservationServiceResponse.builder().result(true).build();
    }
}
