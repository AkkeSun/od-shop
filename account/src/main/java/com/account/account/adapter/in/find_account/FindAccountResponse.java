package com.account.account.adapter.in.find_account;

import com.account.account.applicaiton.service.find_account.FindAccountServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class FindAccountResponse {

    private Long id;
    private String email;
    private String username;
    private String userTel;
    private String address;
    private String role;
    private String regDate;

    @Builder
    public FindAccountResponse(Long id, String email, String username, String userTel,
        String address, String role, String regDate) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.userTel = userTel;
        this.address = address;
        this.role = role;
        this.regDate = regDate;
    }

    FindAccountResponse of(FindAccountServiceResponse serviceResponse) {
        return FindAccountResponse.builder()
            .id(serviceResponse.id())
            .email(serviceResponse.email())
            .username(serviceResponse.username())
            .userTel(serviceResponse.userTel())
            .address(serviceResponse.address())
            .regDate(serviceResponse.regDate())
            .role(serviceResponse.role())
            .build();
    }
}
