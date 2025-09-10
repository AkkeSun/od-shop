package com.product.adapter.in.controller.increase_product_quantity;

import com.product.application.service.Increase_product_quantity.IncreaseProductQuantityServiceResponse;

record IncreaseProductQuantityResponse(
    Boolean result
) {

    static IncreaseProductQuantityResponse of(
        IncreaseProductQuantityServiceResponse serviceResponse
    ) {
        return new IncreaseProductQuantityResponse(serviceResponse.result());
    }
}
