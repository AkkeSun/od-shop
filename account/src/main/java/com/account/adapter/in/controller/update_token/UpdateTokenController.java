package com.account.adapter.in.controller.update_token;

import com.account.applicaiton.port.in.UpdateTokenUseCase;
import com.account.applicaiton.service.update_token.UpdateTokenServiceResponse;
import com.common.infrastructure.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class UpdateTokenController {

    private final UpdateTokenUseCase registerTokenByRefreshUseCase;

    @PutMapping("/auth")
    ApiResponse<UpdateTokenResponse> update(@RequestBody @Valid UpdateTokenRequest request) {
        UpdateTokenServiceResponse serviceResponse = registerTokenByRefreshUseCase
            .update(request.getRefreshToken());

        return ApiResponse.ok(UpdateTokenResponse.of(serviceResponse));
    }
}
