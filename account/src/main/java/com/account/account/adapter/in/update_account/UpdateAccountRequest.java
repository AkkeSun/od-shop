package com.account.account.adapter.in.update_account;

import com.account.account.applicaiton.port.in.command.UpdateAccountCommand;
import com.account.global.validation.ValidPassword;
import com.account.global.validation.ValidUserTel;
import com.account.global.validation.ValidationGroups.CustomGroups;
import com.account.global.validation.ValidationGroups.SizeGroups;
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
}
