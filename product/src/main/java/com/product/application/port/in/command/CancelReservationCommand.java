package com.product.application.port.in.command;

import lombok.Builder;

@Builder
public record CancelReservationCommand(
    Long productId,
    Long reserveId
) {

}
