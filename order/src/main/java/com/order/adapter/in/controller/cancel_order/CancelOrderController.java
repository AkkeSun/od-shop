package com.order.adapter.in.controller.cancel_order;

import com.order.applicatoin.port.in.CancelOrderUseCase;
import com.order.applicatoin.port.in.command.CancelOrderCommand;
import com.order.applicatoin.service.cancel_order.CancelOrderServiceResponse;
import com.order.domain.model.Account;
import com.order.infrastructure.resolver.LoginAccount;
import com.order.infrastructure.response.ApiResponse;
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
        @LoginAccount Account account,
        @PathVariable Long orderId
    ) {
        CancelOrderServiceResponse serviceResponse = useCase.cancel(CancelOrderCommand.builder()
            .orderId(orderId)
            .account(account)
            .build());

        return ApiResponse.ok(CancelOrderResponse.of(serviceResponse));
    }
}
