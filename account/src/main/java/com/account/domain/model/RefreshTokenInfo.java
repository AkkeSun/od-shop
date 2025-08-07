package com.account.domain.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RefreshTokenInfo {

    private Long id;
    private Long accountId;
    private String email;
    private String userAgent;
    private String refreshToken;
    private List<String> roles;

    @Builder
    public RefreshTokenInfo(Long id, Long accountId, String email, String userAgent,
        String refreshToken, List<String> roles) {
        this.id = id;
        this.accountId = accountId;
        this.email = email;
        this.userAgent = userAgent;
        this.refreshToken = refreshToken;
        this.roles = roles;
    }

    public boolean isDifferentRefreshToken(String refreshToken) {
        return !this.refreshToken.equals(refreshToken);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Account toAccount() {
        return Account.builder()
            .id(accountId)
            .email(email)
            .roles(roles.stream()
                .map(role -> Role.builder().name(role).build())
                .toList()
            )
            .build();
    }
}
