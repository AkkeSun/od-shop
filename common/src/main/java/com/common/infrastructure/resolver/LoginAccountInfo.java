package com.common.infrastructure.resolver;

import io.jsonwebtoken.Claims;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginAccountInfo {

    private Long id;
    private String email;
    private List<String> roles;

    public static LoginAccountInfo of(Claims claims) {
        return LoginAccountInfo.builder()
            .email(claims.getSubject())
            .id(Long.valueOf(claims.get("accountId").toString()))
            .roles(Arrays.stream(claims.get("roles").toString().split(","))
                .toList())
            .build();
    }
}
