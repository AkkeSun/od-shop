package com.account.account.adapter.in.delete_account;

import com.account.account.applicaiton.port.in.DeleteAccountUseCase;
import com.account.account.applicaiton.service.delete_account.DeleteAccountServiceResponse;
import com.account.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class DeleteAccountController {

    private final DeleteAccountUseCase deleteAccountUseCase;

    @DeleteMapping("/accounts")
    ApiResponse<DeleteAccountResponse> deleteAccount(
        @RequestHeader(name = "Authorization") String authorization) {
        DeleteAccountServiceResponse serviceResponse = deleteAccountUseCase
            .deleteAccount(authorization);

        return ApiResponse.ok(new DeleteAccountResponse().of(serviceResponse));
    }
}
