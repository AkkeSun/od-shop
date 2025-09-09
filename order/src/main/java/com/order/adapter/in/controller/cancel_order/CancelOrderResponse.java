package com.order.adapter.in.controller.cancel_order;

import com.order.applicatoin.service.cancel_order.CancelOrderServiceResponse;

record CancelOrderResponse(
    Boolean result
) {

    public static CancelOrderResponse of(CancelOrderServiceResponse serviceResponse) {
        return new CancelOrderResponse(serviceResponse.result());
    }
}
