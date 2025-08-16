package com.order.adapter.in.controller.reserve_product;

import com.order.applicatoin.service.reserve_product.ReserveProductServiceResponse;
import java.util.List;
import lombok.Builder;

@Builder
record ReserveProductResponse(
    List<Long> reserveIds
) {

    static ReserveProductResponse of(ReserveProductServiceResponse serviceResponse) {
        return ReserveProductResponse.builder()
            .reserveIds(serviceResponse.reservationIds())
            .build();
    }
}
