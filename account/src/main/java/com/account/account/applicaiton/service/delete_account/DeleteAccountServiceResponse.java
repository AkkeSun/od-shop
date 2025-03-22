package com.account.account.applicaiton.service.delete_account;

import lombok.Builder;

@Builder
public record DeleteAccountServiceResponse(
    Long id, String result
) {

}
