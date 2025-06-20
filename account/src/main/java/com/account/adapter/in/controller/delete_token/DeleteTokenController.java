package com.account.adapter.in.controller.delete_token;

import com.account.applicaiton.port.in.DeleteTokenUseCase;
import com.account.applicaiton.service.delete_token.DeleteTokenServiceResponse;
import com.account.domain.model.Account;
import com.account.infrastructure.resolver.LoginAccount;
import com.account.infrastructure.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class DeleteTokenController {

    private final DeleteTokenUseCase deleteTokenUseCase;

    @DeleteMapping("/auth")
    ApiResponse<DeleteTokenResponse> deleteToken(@LoginAccount Account account) {
        DeleteTokenServiceResponse serviceResponse = deleteTokenUseCase.deleteToken(account);

        return ApiResponse.ok(new DeleteTokenResponse().of(serviceResponse));
    }
}
