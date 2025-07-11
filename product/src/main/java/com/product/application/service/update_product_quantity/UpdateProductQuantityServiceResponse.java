package com.product.application.service.update_product_quantity;

import grpc.product.UpdateProductQuantityStubResponse;
import lombok.Builder;

@Builder
public record UpdateProductQuantityServiceResponse(
    Boolean result
) {

    public UpdateProductQuantityStubResponse toStubResponse() {
        return UpdateProductQuantityStubResponse.newBuilder()
            .setResult(result)
            .build();
    }
}
