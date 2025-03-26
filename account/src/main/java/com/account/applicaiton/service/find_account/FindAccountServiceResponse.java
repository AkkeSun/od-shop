package com.account.applicaiton.service.find_account;

import lombok.Builder;

@Builder
public record FindAccountServiceResponse(
    Long id,
    String email,
    String username,
    String userTel,
    String address,
    String regDate,
    String role
) {

}
