package com.account.adapter.in.controller.update_account;

import com.account.applicaiton.port.in.UpdateAccountUseCase;
import com.account.applicaiton.service.update_account.UpdateAccountServiceResponse;
import com.account.domain.model.Account;
import com.account.infrastructure.resolver.LoginAccount;
import com.account.infrastructure.response.ApiResponse;
import com.account.infrastructure.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class UpdateAccountController {

    private final UpdateAccountUseCase updateAccountUseCase;

    @PutMapping("/accounts")
    ApiResponse<UpdateAccountResponse> updateAccount(
        @RequestBody @Validated(ValidationSequence.class) UpdateAccountRequest request,
        @LoginAccount Account account) {
        UpdateAccountServiceResponse serviceResponse = updateAccountUseCase
            .updateAccount(request.toCommand(account.getId()));

        return ApiResponse.ok(UpdateAccountResponse.of(serviceResponse));
    }
}
