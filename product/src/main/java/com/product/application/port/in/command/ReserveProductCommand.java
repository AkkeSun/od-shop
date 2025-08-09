package com.product.application.port.in.command;

import lombok.Builder;

@Builder
public record ReserveProductCommand(
    Long productId,
    Long productQuantity,
    Long customerId
) {
}
