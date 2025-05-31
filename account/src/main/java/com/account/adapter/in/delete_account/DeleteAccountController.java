package com.account.adapter.in.delete_account;

import com.account.applicaiton.port.in.DeleteAccountUseCase;
import com.account.applicaiton.service.delete_account.DeleteAccountServiceResponse;
import com.account.domain.model.Account;
import com.account.infrastructure.resolver.LoginAccount;
import com.account.infrastructure.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class DeleteAccountController {

    private final DeleteAccountUseCase deleteAccountUseCase;

    @DeleteMapping("/accounts")
    ApiResponse<DeleteAccountResponse> deleteAccount(@LoginAccount Account account) {
        DeleteAccountServiceResponse serviceResponse = deleteAccountUseCase
            .deleteAccount(account);

        return ApiResponse.ok(new DeleteAccountResponse().of(serviceResponse));
    }
}
