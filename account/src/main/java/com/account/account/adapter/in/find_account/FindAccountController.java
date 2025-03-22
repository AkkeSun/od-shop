package com.account.account.adapter.in.find_account;

import com.account.account.applicaiton.port.in.FindAccountInfoUseCase;
import com.account.account.applicaiton.service.find_account.FindAccountServiceResponse;
import com.account.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
class FindAccountController {

    private final FindAccountInfoUseCase findAccountInfoUseCase;

    @GetMapping("/accounts")
    ApiResponse<FindAccountResponse> findAccountInfo(
        @RequestHeader(name = "Authorization", required = false) String authorization) {
        FindAccountServiceResponse serviceResponse = findAccountInfoUseCase.
            findAccountInfo(authorization);

        return ApiResponse.ok(new FindAccountResponse().of(serviceResponse));
    }
}
