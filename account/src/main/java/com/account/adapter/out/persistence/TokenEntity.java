package com.account.adapter.out.persistence;

import com.account.domain.model.Token;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "TOKEN")
@NoArgsConstructor
class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "USER_AGENT")
    private String userAgent;

    @Column(name = "REFRESH_TOKEN")
    private String refreshToken;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "REG_DATE_TIME")
    private String regDateTime;

    @Builder
    TokenEntity(Long id, Long accountId, String email, String userAgent, String refreshToken,
        String role, String regDateTime) {
        this.id = id;
        this.accountId = accountId;
        this.email = email;
        this.userAgent = userAgent;
        this.refreshToken = refreshToken;
        this.role = role;
        this.regDateTime = regDateTime;
    }

    void updateByDomain(Token tokenCache) {
        this.refreshToken = tokenCache.getRefreshToken();
        this.regDateTime = tokenCache.getRegDateTime();
        this.userAgent = tokenCache.getUserAgent();
    }
}
