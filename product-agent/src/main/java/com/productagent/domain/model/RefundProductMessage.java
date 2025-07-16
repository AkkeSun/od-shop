package com.productagent.domain.model;

import grpc.product.UpdateProductQuantityStubRequest;
import lombok.Builder;

@Builder
public record RefundProductMessage(
    Long productId,
    long quantity
) {

    public UpdateProductQuantityStubRequest toClientMessage() {
        return UpdateProductQuantityStubRequest.newBuilder()
            .setProductId(productId)
            .setQuantity(quantity)
            .setQuantityType("REFUND")
            .build();
    }
}
