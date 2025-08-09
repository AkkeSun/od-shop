package com.product.application.service.Increase_product_quantity;

import lombok.Builder;

@Builder
public record IncreaseProductQuantityServiceResponse(
    Boolean result
) {

    public static IncreaseProductQuantityServiceResponse ofSuccess() {
        return IncreaseProductQuantityServiceResponse.builder()
            .result(true)
            .build();
    }
}
