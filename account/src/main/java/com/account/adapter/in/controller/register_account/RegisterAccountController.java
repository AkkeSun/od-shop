package com.account.adapter.in.controller.register_account;

import com.account.applicaiton.port.in.RegisterAccountUseCase;
import com.account.applicaiton.service.register_account.RegisterAccountServiceResponse;
import com.common.infrastructure.response.ApiResponse;
import com.common.infrastructure.validation.groups.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegisterAccountController {

    private final RegisterAccountUseCase registerAccountUseCase;

    @PostMapping("/accounts")
    ApiResponse<RegisterAccountResponse> registerAccount(
        @RequestBody @Validated(ValidationSequence.class) RegisterAccountRequest request
    ) {
        RegisterAccountServiceResponse serviceResponse = registerAccountUseCase
            .registerAccount(request.toCommand());

        return ApiResponse.ok(RegisterAccountResponse.of(serviceResponse));
    }
}
