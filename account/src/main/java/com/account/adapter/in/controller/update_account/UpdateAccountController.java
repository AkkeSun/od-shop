package com.account.adapter.in.controller.update_account;

import com.account.applicaiton.port.in.UpdateAccountUseCase;
import com.account.applicaiton.service.update_account.UpdateAccountServiceResponse;
import com.common.infrastructure.resolver.LoginAccount;
import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.response.ApiResponse;
import com.common.infrastructure.validation.groups.ValidationSequence;
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
        @LoginAccount LoginAccountInfo loginInfo
    ) {
        UpdateAccountServiceResponse serviceResponse = updateAccountUseCase
            .updateAccount(request.toCommand(loginInfo.getId()));

        return ApiResponse.ok(UpdateAccountResponse.of(serviceResponse));
    }
}
