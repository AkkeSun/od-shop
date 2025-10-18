package com.account.adapter.in.controller.update_token;

import static com.common.infrastructure.util.JsonUtil.toJsonString;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UpdateTokenRequest {

    @NotBlank(message = "리프레시 토큰은 필수값 입니다.")
    private String refreshToken;

    @Override
    public String toString() {
        return toJsonString(this);
    }
}
