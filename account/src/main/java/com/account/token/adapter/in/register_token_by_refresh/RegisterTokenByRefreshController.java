package com.account.token.adapter.in.register_token_by_refresh;

import com.account.global.response.ApiResponse;
import com.account.token.application.port.in.RegisterTokenByRefreshUseCase;
import com.account.token.application.service.register_token_by_refresh.RegisterTokenByRefreshServiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegisterTokenByRefreshController {

    private final RegisterTokenByRefreshUseCase registerTokenByRefreshUseCase;

    @PostMapping("/auth/refresh")
    ApiResponse<RegisterTokenByRefreshResponse> registerToken(
        @RequestBody @Valid RegisterTokenByRefreshRequest request) {
        RegisterTokenByRefreshServiceResponse serviceResponse = registerTokenByRefreshUseCase
            .registerTokenByRefresh(request.getRefreshToken());

        return ApiResponse.ok(new RegisterTokenByRefreshResponse().of(serviceResponse));
    }
}
