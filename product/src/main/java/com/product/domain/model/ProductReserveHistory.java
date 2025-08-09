package com.product.domain.model;

import com.product.application.port.in.command.CreateReservationCommand;
import lombok.Builder;

@Builder
public record ProductReserveHistory(
    Long id,
    Long productId,
    Long customerId,
    long reservedQuantity
) {

    public static ProductReserveHistory of(Long id, CreateReservationCommand command) {
        return ProductReserveHistory.builder()
            .id(id)
            .productId(command.productId())
            .customerId(command.customerId())
            .reservedQuantity(command.productQuantity())
            .build();
    }
}
