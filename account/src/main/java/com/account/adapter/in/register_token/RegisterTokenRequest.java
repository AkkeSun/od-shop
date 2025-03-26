package com.account.adapter.in.register_token;

import com.account.applicaiton.port.in.command.RegisterTokenCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class RegisterTokenRequest {

    @NotBlank(message = "이메일은 필수값 입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수값 입니다.")
    private String password;

    @Builder
    RegisterTokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    RegisterTokenCommand toCommand() {
        return RegisterTokenCommand.builder()
            .email(email)
            .password(password)
            .build();
    }

}
