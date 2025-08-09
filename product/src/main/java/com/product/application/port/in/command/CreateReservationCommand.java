package com.product.application.port.in.command;

import lombok.Builder;

@Builder
public record CreateReservationCommand(
    Long productId,
    Long productQuantity,
    Long customerId
) {
}
