package com.account.adapter.in.delete_token;

import com.account.applicaiton.port.in.DeleteTokenUseCase;
import com.account.applicaiton.service.delete_token.DeleteTokenServiceResponse;
import com.account.infrastructure.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class DeleteTokenController {

    private final DeleteTokenUseCase deleteTokenUseCase;

    @DeleteMapping("/auth")
    ApiResponse<DeleteTokenResponse> deleteToken(
        @RequestHeader(name = "Authorization", required = false) String accessToken) {
        DeleteTokenServiceResponse serviceResponse = deleteTokenUseCase.deleteToken(accessToken);

        return ApiResponse.ok(new DeleteTokenResponse().of(serviceResponse));
    }
}
