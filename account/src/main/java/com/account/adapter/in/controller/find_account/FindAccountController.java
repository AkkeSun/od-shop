package com.account.adapter.in.controller.find_account;

import com.account.applicaiton.port.in.FindAccountInfoUseCase;
import com.account.applicaiton.service.find_account.FindAccountServiceResponse;
import com.account.domain.model.Account;
import com.account.infrastructure.resolver.LoginAccount;
import com.account.infrastructure.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
class FindAccountController {

    private final FindAccountInfoUseCase findAccountInfoUseCase;

    @GetMapping("/accounts")
    ApiResponse<FindAccountResponse> findAccountInfo(@LoginAccount Account account) {
        FindAccountServiceResponse serviceResponse = findAccountInfoUseCase.
            findAccountInfo(account);

        return ApiResponse.ok(FindAccountResponse.of(serviceResponse));
    }
}
