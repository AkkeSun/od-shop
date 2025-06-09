package com.account.applicaiton.service.find_account;

import com.account.domain.model.Account;
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

    public static FindAccountServiceResponse of(Account account) {
        return FindAccountServiceResponse.builder()
            .id(account.getId())
            .email(account.getEmail())
            .username(account.getUsername())
            .userTel(account.getUserTel())
            .address(account.getAddress())
            .regDate(account.getRegDate())
            .role(account.getRole().toString())
            .build();
    }
}
