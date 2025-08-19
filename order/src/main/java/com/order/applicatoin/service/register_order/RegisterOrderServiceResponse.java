package com.order.applicatoin.service.register_order;

import com.order.domain.model.Order;
import lombok.Builder;

@Builder
public record RegisterOrderServiceResponse(
    Boolean result,
    Long orderNumber
) {
    static RegisterOrderServiceResponse of(Order order){
        return RegisterOrderServiceResponse.builder()
            .result(true)
            .orderNumber(order.orderNumber())
            .build();
    }
}
