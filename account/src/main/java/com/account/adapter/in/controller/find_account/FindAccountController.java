package com.account.adapter.in.controller.find_account;

import com.account.applicaiton.port.in.FindAccountInfoUseCase;
import com.account.applicaiton.service.find_account.FindAccountServiceResponse;
import com.common.infrastructure.resolver.LoginAccount;
import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.response.ApiResponse;
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
    ApiResponse<FindAccountResponse> findAccountInfo(@LoginAccount LoginAccountInfo loginInfo) {
        FindAccountServiceResponse serviceResponse = findAccountInfoUseCase.
            findAccountInfo(loginInfo);

        return ApiResponse.ok(FindAccountResponse.of(serviceResponse));
    }
}
