package com.account.adapter.in.controller.find_account;

import com.account.applicaiton.service.find_account.FindAccountServiceResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class FindAccountResponse {

    private Long id;
    private String email;
    private String username;
    private String userTel;
    private String address;
    private List<String> roles;
    private String regDate;

    static FindAccountResponse of(FindAccountServiceResponse serviceResponse) {
        return FindAccountResponse.builder()
            .id(serviceResponse.id())
            .email(serviceResponse.email())
            .username(serviceResponse.username())
            .userTel(serviceResponse.userTel())
            .address(serviceResponse.address())
            .regDate(serviceResponse.regDate())
            .roles(serviceResponse.roles())
            .build();
    }
}
