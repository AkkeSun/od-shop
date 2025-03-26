package com.account.adapter.in.register_token_by_refresh;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class RegisterTokenByRefreshRequest {

    @NotBlank(message = "리프레시 토큰은 필수값 입니다.")
    private String refreshToken;

    @Builder
    public RegisterTokenByRefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
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
