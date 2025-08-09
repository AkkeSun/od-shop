package com.product.application.port.in.command;

import grpc.product.ReserveProductStubRequest;
import lombok.Builder;

@Builder
public record ReserveProductCommand(
    Long productId,
    Long productQuantity,
    Long customerId
) {

    public static ReserveProductCommand of(ReserveProductStubRequest request) {
        return ReserveProductCommand.builder()
            .productId(request.getProductId())
            .productQuantity(request.getQuantity())
            .customerId(request.getCustomerId())
            .build();
    }
}
