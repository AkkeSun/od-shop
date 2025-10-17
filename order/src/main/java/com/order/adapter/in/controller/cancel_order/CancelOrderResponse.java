package com.order.adapter.in.controller.cancel_order;

import com.order.application.service.cancel_order.CancelOrderServiceResponse;

record CancelOrderResponse(
    Boolean result
) {

    static CancelOrderResponse of(CancelOrderServiceResponse serviceResponse) {
        return new CancelOrderResponse(serviceResponse.result());
    }
}
