package com.account.adapter.in.update_account;

import com.account.applicaiton.port.in.command.UpdateAccountCommand;
import com.account.infrastructure.validation.ValidPassword;
import com.account.infrastructure.validation.ValidUserTel;
import com.account.infrastructure.validation.ValidationGroups.CustomGroups;
import com.account.infrastructure.validation.ValidationGroups.SizeGroups;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ValidPassword(groups = CustomGroups.class)
class UpdateAccountRequest {

    private String password;

    private String passwordCheck;

    @Size(max = 10, message = "이름은 10자 이하로 입력 가능 합니다.", groups = SizeGroups.class)
    private String username;

    @ValidUserTel(groups = CustomGroups.class)
    private String userTel;

    @Size(max = 100, message = "주소는 100자 이하로 입력 가능 합니다.", groups = SizeGroups.class)
    private String address;

    @Builder
    UpdateAccountRequest(String password, String passwordCheck, String username,
        String userTel, String address) {
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.username = username;
        this.userTel = userTel;
        this.address = address;
    }

    UpdateAccountCommand toCommand(String accessToken) {
        return UpdateAccountCommand.builder()
            .accessToken(accessToken)
            .password(password)
            .passwordCheck(passwordCheck)
            .username(username)
            .userTel(userTel)
            .address(address)
            .build();
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }
}
