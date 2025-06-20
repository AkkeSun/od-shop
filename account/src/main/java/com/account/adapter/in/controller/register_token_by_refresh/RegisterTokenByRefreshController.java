package com.account.adapter.in.controller.register_token_by_refresh;

import com.account.applicaiton.port.in.RegisterTokenByRefreshUseCase;
import com.account.applicaiton.service.register_token_by_refresh.RegisterTokenByRefreshServiceResponse;
import com.account.infrastructure.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegisterTokenByRefreshController {

    private final RegisterTokenByRefreshUseCase registerTokenByRefreshUseCase;

    @PutMapping("/auth")
    ApiResponse<RegisterTokenByRefreshResponse> registerToken(
        @RequestBody @Valid RegisterTokenByRefreshRequest request) {
        RegisterTokenByRefreshServiceResponse serviceResponse = registerTokenByRefreshUseCase
            .registerTokenByRefresh(request.getRefreshToken());

        return ApiResponse.ok(new RegisterTokenByRefreshResponse().of(serviceResponse));
    }
}
