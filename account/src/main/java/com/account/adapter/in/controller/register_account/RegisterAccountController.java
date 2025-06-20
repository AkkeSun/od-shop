package com.account.adapter.in.controller.register_account;

import com.account.applicaiton.port.in.RegisterAccountUseCase;
import com.account.applicaiton.service.register_account.RegisterAccountServiceResponse;
import com.account.infrastructure.response.ApiResponse;
import com.account.infrastructure.validation.ValidationSequence;
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
        @RequestBody @Validated(ValidationSequence.class) RegisterAccountRequest request) {
        RegisterAccountServiceResponse serviceResponse = registerAccountUseCase
            .registerAccount(request.toCommand());

        return ApiResponse.ok(new RegisterAccountResponse().of(serviceResponse));
    }
}
