package com.account.applicaiton.service.delete_account;

import com.account.domain.model.Account;
import lombok.Builder;

@Builder
public record DeleteAccountServiceResponse(
    Long id, String result
) {

    public static DeleteAccountServiceResponse ofSuccess(Account account) {
        return DeleteAccountServiceResponse.builder()
            .id(account.getId())
            .result("Y")
            .build();
    }
}
