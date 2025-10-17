package com.order.adapter.in.controller.register_order;

import com.order.application.port.in.RegisterOrderUseCase;
import com.order.application.service.register_order.RegisterOrderServiceResponse;
import com.order.domain.model.Account;
import com.order.infrastructure.resolver.LoginAccount;
import com.order.infrastructure.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegisterOrderController {

    private final RegisterOrderUseCase useCase;

    @PostMapping("/orders")
    ApiResponse<RegisterOrderResponse> register(
        @RequestBody @Valid RegisterOrderRequest request,
        @LoginAccount Account account
    ) {
        RegisterOrderServiceResponse serviceResponse = useCase.register(request.toCommand(account));

        return ApiResponse.ok(RegisterOrderResponse.of(serviceResponse));
    }
}
