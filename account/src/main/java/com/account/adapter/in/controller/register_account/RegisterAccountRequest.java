package com.account.adapter.in.controller.register_account;

import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.account.applicaiton.port.in.command.RegisterAccountCommand;
import com.common.infrastructure.validation.ValidPassword;
import com.common.infrastructure.validation.ValidUserTel;
import com.common.infrastructure.validation.ValidationGroups.CustomGroups;
import com.common.infrastructure.validation.ValidationGroups.NotBlankGroups;
import com.common.infrastructure.validation.ValidationGroups.SizeGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidPassword(groups = CustomGroups.class)
class RegisterAccountRequest {

    @NotBlank(message = "이메일은 필수값 입니다.", groups = NotBlankGroups.class)
    @Size(max = 50, message = "이메일은 50자 이하로 입력 가능 합니다.", groups = SizeGroups.class)
    private String email;

    @NotBlank(message = "비밀번호는 필수값 입니다.", groups = NotBlankGroups.class)
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수값 입니다.", groups = NotBlankGroups.class)
    private String passwordCheck;

    @NotNull(message = "권한은 필수값 입니다.", groups = NotBlankGroups.class)
    private List<String> roles;

    @Size(max = 10, message = "이름은 10자 이하로 입력 가능 합니다.", groups = SizeGroups.class)
    private String username;

    @ValidUserTel(groups = CustomGroups.class)
    private String userTel;

    @Size(max = 100, message = "주소는 100자 이하로 입력 가능 합니다.", groups = SizeGroups.class)
    private String address;

    RegisterAccountCommand toCommand() {
        return RegisterAccountCommand.builder()
            .email(email)
            .password(password)
            .username(username)
            .userTel(userTel)
            .address(address)
            .roles(roles)
            .build();
    }

    @Override
    public String toString() {
        return toJsonString(this);
    }
}
