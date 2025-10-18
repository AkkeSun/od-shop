package com.account.adapter.in.controller.register_token;

import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.account.applicaiton.port.in.command.RegisterTokenCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class RegisterTokenRequest {

    @NotBlank(message = "이메일은 필수값 입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수값 입니다.")
    private String password;

    RegisterTokenCommand toCommand() {
        return RegisterTokenCommand.builder()
            .email(email)
            .password(password)
            .build();
    }
    
    @Override
    public String toString() {
        return toJsonString(this);
    }
}
