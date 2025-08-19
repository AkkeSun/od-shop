package com.order.adapter.in.controller.register_order;

import lombok.Builder;

@Builder
record RegisterOrderResponse(
    Boolean result,
    Long orderNumber
) {

}
