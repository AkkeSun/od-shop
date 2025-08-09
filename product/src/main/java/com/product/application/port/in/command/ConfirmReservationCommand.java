package com.product.application.port.in.command;

import lombok.Builder;

@Builder
public record ConfirmReservationCommand(
    Long productId,
    Long reserveId
) {

}
