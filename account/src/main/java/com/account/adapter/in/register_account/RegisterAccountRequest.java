package com.account.adapter.in.register_account;

import com.account.applicaiton.port.in.command.RegisterAccountCommand;
import com.account.infrastructure.validation.ValidPassword;
import com.account.infrastructure.validation.ValidRole;
import com.account.infrastructure.validation.ValidUserTel;
import com.account.infrastructure.validation.ValidationGroups.CustomGroups;
import com.account.infrastructure.validation.ValidationGroups.NotBlankGroups;
import com.account.infrastructure.validation.ValidationGroups.SizeGroups;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ValidPassword(groups = CustomGroups.class)
class RegisterAccountRequest {

    @NotBlank(message = "이메일은 필수값 입니다.", groups = NotBlankGroups.class)
    @Size(max = 50, message = "이메일은 50자 이하로 입력 가능 합니다.", groups = SizeGroups.class)
    private String email;

    @NotBlank(message = "비밀번호는 필수값 입니다.", groups = NotBlankGroups.class)
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수값 입니다.", groups = NotBlankGroups.class)
    private String passwordCheck;

    @ValidRole(groups = CustomGroups.class)
    @NotBlank(message = "권한은 필수값 입니다.", groups = NotBlankGroups.class)
    private String role;

    @Size(max = 10, message = "이름은 10자 이하로 입력 가능 합니다.", groups = SizeGroups.class)
    private String username;

    @ValidUserTel(groups = CustomGroups.class)
    private String userTel;

    @Size(max = 100, message = "주소는 100자 이하로 입력 가능 합니다.", groups = SizeGroups.class)
    private String address;

    @Builder
    RegisterAccountRequest(String email, String password, String passwordCheck,
        String username,
        String userTel, String address, String role) {
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.username = username;
        this.userTel = userTel;
        this.address = address;
        this.role = role;
    }

    RegisterAccountCommand toCommand() {
        return RegisterAccountCommand.builder()
            .email(email)
            .password(password)
            .username(username)
            .userTel(userTel)
            .address(address)
            .role(role)
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
