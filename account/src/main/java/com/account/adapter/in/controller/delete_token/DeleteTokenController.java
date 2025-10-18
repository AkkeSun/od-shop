package com.account.adapter.in.controller.delete_token;

import com.account.applicaiton.port.in.DeleteTokenUseCase;
import com.account.applicaiton.service.delete_token.DeleteTokenServiceResponse;
import com.common.infrastructure.resolver.LoginAccount;
import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class DeleteTokenController {

    private final DeleteTokenUseCase deleteTokenUseCase;

    @DeleteMapping("/auth")
    ApiResponse<DeleteTokenResponse> deleteToken(@LoginAccount LoginAccountInfo loginInfo) {
        DeleteTokenServiceResponse serviceResponse = deleteTokenUseCase.deleteToken(loginInfo);

        return ApiResponse.ok(DeleteTokenResponse.of(serviceResponse));
    }
}
