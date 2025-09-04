package com.order.adapter.in.controller.cancel_order;

import com.order.domain.model.Account;
import com.order.infrastructure.resolver.LoginAccount;
import com.order.infrastructure.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class CancelOrderController {

    @DeleteMapping("/orders/{orderId}")
    ApiResponse<CancelOrderResponse> cancel(@LoginAccount Account account) {
        return ApiResponse.ok(null);
    }
}
