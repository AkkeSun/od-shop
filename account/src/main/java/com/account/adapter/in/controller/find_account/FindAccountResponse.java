package com.account.adapter.in.controller.find_account;

import com.account.applicaiton.service.find_account.FindAccountServiceResponse;
import java.util.List;
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
    private List<String> roles;
    private String regDate;

    @Builder
    public FindAccountResponse(Long id, String email, String username, String userTel,
        String address, List<String> roles, String regDate) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.userTel = userTel;
        this.address = address;
        this.roles = roles;
        this.regDate = regDate;
    }

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
