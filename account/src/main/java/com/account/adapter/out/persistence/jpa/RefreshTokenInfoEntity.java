package com.account.adapter.out.persistence.jpa;

import com.account.domain.model.RefreshTokenInfo;
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
@Table(name = "REFRESH_TOKEN_INFO")
@NoArgsConstructor
class RefreshTokenInfoEntity {

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

    @Column(name = "ROLES")
    private String roles;

    @Column(name = "REG_DATE_TIME")
    private String regDateTime;

    @Builder
    RefreshTokenInfoEntity(Long id, Long accountId, String email, String userAgent,
        String refreshToken,
        String roles, String regDateTime) {
        this.id = id;
        this.accountId = accountId;
        this.email = email;
        this.userAgent = userAgent;
        this.refreshToken = refreshToken;
        this.roles = roles;
        this.regDateTime = regDateTime;
    }

    void updateByDomain(RefreshTokenInfo domain) {
        this.refreshToken = domain.getRefreshToken();
        this.regDateTime = domain.getRegDateTime();
        this.userAgent = domain.getUserAgent();
        this.roles = domain.getRoles();
    }
}
