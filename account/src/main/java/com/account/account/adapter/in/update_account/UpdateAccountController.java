package com.account.account.adapter.in.update_account;

import com.account.account.applicaiton.port.in.UpdateAccountUseCase;
import com.account.account.applicaiton.service.update_account.UpdateAccountServiceResponse;
import com.account.global.response.ApiResponse;
import com.account.global.validation.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class UpdateAccountController {

    private final UpdateAccountUseCase updateAccountUseCase;

    @PutMapping("/accounts")
    ApiResponse<UpdateAccountResponse> updateAccount(
        @RequestBody @Validated(ValidationSequence.class) UpdateAccountRequest request,
        @RequestHeader(name = "Authorization", required = false) String authorization) {
        UpdateAccountServiceResponse serviceResponse = updateAccountUseCase
            .updateAccount(request.toCommand(authorization));

        return ApiResponse.ok(new UpdateAccountResponse().of(serviceResponse));
    }
}
