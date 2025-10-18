package com.account.domain.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenInfo {

    private Long id;
    private Long accountId;
    private String email;
    private String userAgent;
    private String refreshToken;
    private List<String> roles;
    
    public boolean isDifferentRefreshToken(String refreshToken) {
        return !this.refreshToken.equals(refreshToken);
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public String getRoleString() {
        return String.join(",", roles);
    }
}
