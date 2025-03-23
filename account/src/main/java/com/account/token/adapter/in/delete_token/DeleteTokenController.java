package com.account.token.adapter.in.delete_token;

import com.account.global.response.ApiResponse;
import com.account.token.application.port.in.DeleteTokenUseCase;
import com.account.token.application.service.delete_token.DeleteTokenServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class DeleteTokenController {

    private final DeleteTokenUseCase deleteTokenUseCase;

    @DeleteMapping("/auth/logout")
    ApiResponse<DeleteTokenResponse> deleteToken(
        @RequestHeader(name = "Authorization", required = false) String accessToken) {
        DeleteTokenServiceResponse serviceResponse = deleteTokenUseCase.deleteToken(accessToken);

        return ApiResponse.ok(new DeleteTokenResponse().of(serviceResponse));
    }
}
