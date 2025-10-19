package com.order.adapter.in.controller.cancel_order;

import com.common.infrastructure.resolver.LoginAccount;
import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.response.ApiResponse;
import com.order.application.port.in.CancelOrderUseCase;
import com.order.application.port.in.command.CancelOrderCommand;
import com.order.application.service.cancel_order.CancelOrderServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class CancelOrderController {

    private final CancelOrderUseCase useCase;

    @DeleteMapping("/orders/{orderId}")
    ApiResponse<CancelOrderResponse> cancel(
        @LoginAccount LoginAccountInfo loginInfo,
        @PathVariable Long orderId
    ) {
        CancelOrderServiceResponse serviceResponse = useCase.cancel(CancelOrderCommand.builder()
            .orderId(orderId)
            .account(loginInfo)
            .build());

        return ApiResponse.ok(CancelOrderResponse.of(serviceResponse));
    }
}
