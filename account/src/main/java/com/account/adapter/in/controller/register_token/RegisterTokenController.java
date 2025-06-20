package com.account.adapter.in.controller.register_token;

import com.account.applicaiton.port.in.RegisterTokenUseCase;
import com.account.applicaiton.service.register_token.RegisterTokenServiceResponse;
import com.account.infrastructure.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegisterTokenController {

    private final RegisterTokenUseCase registerTokenUseCase;

    @PostMapping("/auth")
    ApiResponse<RegisterTokenResponse> registerToken(
        @RequestBody @Valid RegisterTokenRequest request) {
        RegisterTokenServiceResponse serviceResponse = registerTokenUseCase
            .registerToken(request.toCommand());

        return ApiResponse.ok(new RegisterTokenResponse().of(serviceResponse));
    }
}
