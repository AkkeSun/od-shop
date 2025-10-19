package com.order.adapter.in.controller.register_order;

import com.common.infrastructure.resolver.LoginAccount;
import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.response.ApiResponse;
import com.order.application.port.in.RegisterOrderUseCase;
import com.order.application.service.register_order.RegisterOrderServiceResponse;
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
        @LoginAccount LoginAccountInfo loginInfo
    ) {
        RegisterOrderServiceResponse serviceResponse = useCase.register(
            request.toCommand(loginInfo));

        return ApiResponse.ok(RegisterOrderResponse.of(serviceResponse));
    }
}
