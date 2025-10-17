package com.order.adapter.in.controller.register_order;

import com.order.application.service.register_order.RegisterOrderServiceResponse;
import lombok.Builder;

@Builder
record RegisterOrderResponse(
    Boolean result,
    Long orderNumber
) {

    static RegisterOrderResponse of(RegisterOrderServiceResponse serviceResponse) {
        return RegisterOrderResponse.builder()
            .result(serviceResponse.result())
            .orderNumber(serviceResponse.orderNumber())
            .build();
    }
}
