package com.account.adapter.in.controller.update_account;

import com.account.applicaiton.port.in.command.UpdateAccountCommand;
import com.account.infrastructure.request.BaseRequest;
import com.account.infrastructure.validation.ValidPassword;
import com.account.infrastructure.validation.ValidUserTel;
import com.account.infrastructure.validation.ValidationGroups.CustomGroups;
import com.account.infrastructure.validation.ValidationGroups.SizeGroups;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidPassword(groups = CustomGroups.class)
class UpdateAccountRequest extends BaseRequest {

    private String password;

    private String passwordCheck;

    @Size(max = 10, message = "이름은 10자 이하로 입력 가능 합니다.", groups = SizeGroups.class)
    private String username;

    @ValidUserTel(groups = CustomGroups.class)
    private String userTel;

    @Size(max = 100, message = "주소는 100자 이하로 입력 가능 합니다.", groups = SizeGroups.class)
    private String address;
    
    UpdateAccountCommand toCommand(Long accountId) {
        return UpdateAccountCommand.builder()
            .accountId(accountId)
            .password(password)
            .passwordCheck(passwordCheck)
            .username(username)
            .userTel(userTel)
            .address(address)
            .build();
    }
}
