package com.account.domain.model;

import java.util.Arrays;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Token {

    private Long id;
    private Long accountId;
    private String email;
    private String userAgent;
    private String refreshToken;
    private String regDateTime;
    private String roles;

    @Builder
    public Token(Long id, Long accountId, String email, String userAgent, String refreshToken,
        String regDateTime, String roles) {
        this.id = id;
        this.accountId = accountId;
        this.email = email;
        this.userAgent = userAgent;
        this.refreshToken = refreshToken;
        this.regDateTime = regDateTime;
        this.roles = roles;
    }

    public boolean isDifferentRefreshToken(String refreshToken) {
        return !this.refreshToken.equals(refreshToken);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateRegTime(String regDateTime) {
        this.regDateTime = regDateTime;
    }

    public Account toAccount() {
        return Account.builder()
            .id(accountId)
            .email(email)
            .roles(Arrays.stream(roles.split(","))
                .map(role -> Role.builder().name(role).build())
                .toList()
            )
            .build();
    }
}
