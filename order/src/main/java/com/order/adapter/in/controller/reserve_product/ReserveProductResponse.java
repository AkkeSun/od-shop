package com.order.adapter.in.controller.reserve_product;

import com.order.application.service.reserve_product.ReserveProductServiceResponse;
import lombok.Builder;

@Builder
record ReserveProductResponse(
    Long productId,
    Long reserveId
) {

    static ReserveProductResponse of(ReserveProductServiceResponse serviceResponse) {
        return ReserveProductResponse.builder()
            .productId(serviceResponse.productId())
            .reserveId(serviceResponse.reserveId())
            .build();
    }
}
