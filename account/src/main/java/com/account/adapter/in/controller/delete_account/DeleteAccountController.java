package com.account.adapter.in.controller.delete_account;

import com.account.applicaiton.port.in.DeleteAccountUseCase;
import com.account.applicaiton.service.delete_account.DeleteAccountServiceResponse;
import com.common.infrastructure.resolver.LoginAccount;
import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class DeleteAccountController {

    private final DeleteAccountUseCase deleteAccountUseCase;

    @DeleteMapping("/accounts")
    ApiResponse<DeleteAccountResponse> deleteAccount(@LoginAccount LoginAccountInfo loginInfo) {
        DeleteAccountServiceResponse serviceResponse = deleteAccountUseCase
            .deleteAccount(loginInfo);

        return ApiResponse.ok(DeleteAccountResponse.of(serviceResponse));
    }
}
