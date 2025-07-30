package com.account.applicaiton.service.find_account;

import com.account.domain.model.Account;
import com.account.domain.model.Role;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record FindAccountServiceResponse(
    Long id,
    String email,
    String username,
    String userTel,
    String address,
    String regDate,
    List<String> roles
) {

    public static FindAccountServiceResponse of(Account account) {
        return FindAccountServiceResponse.builder()
            .id(account.getId())
            .email(account.getEmail())
            .username(account.getUsername())
            .userTel(account.getUserTel())
            .address(account.getAddress())
            .regDate(account.getRegDate())
            .roles(account.getRoles().stream().map(Role::name)
                .collect(Collectors.toList()))
            .build();
    }
}
