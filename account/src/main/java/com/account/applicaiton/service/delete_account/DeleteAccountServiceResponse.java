package com.account.applicaiton.service.delete_account;

import com.common.infrastructure.resolver.LoginAccountInfo;
import lombok.Builder;

@Builder
public record DeleteAccountServiceResponse(
    Long id, String result
) {

    public static DeleteAccountServiceResponse ofSuccess(LoginAccountInfo account) {
        return DeleteAccountServiceResponse.builder()
            .id(account.getId())
            .result("Y")
            .build();
    }
}
