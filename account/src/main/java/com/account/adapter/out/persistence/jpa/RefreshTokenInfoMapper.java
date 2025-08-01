package com.account.adapter.out.persistence.jpa;

import com.account.domain.model.RefreshTokenInfo;
import org.springframework.stereotype.Component;

@Component
class RefreshTokenInfoMapper {

    public RefreshTokenInfo toDomain(RefreshTokenInfoEntity entity) {
        return RefreshTokenInfo.builder()
            .id(entity.getId())
            .accountId(entity.getAccountId())
            .email(entity.getEmail())
            .userAgent(entity.getUserAgent())
            .refreshToken(entity.getRefreshToken())
            .roles(entity.getRoles())
            .regDateTime(entity.getRegDateTime())
            .build();
    }

    public RefreshTokenInfoEntity toEntity(RefreshTokenInfo domain) {
        return RefreshTokenInfoEntity.builder()
            .id(domain.getId())
            .accountId(domain.getAccountId())
            .email(domain.getEmail())
            .userAgent(domain.getUserAgent())
            .refreshToken(domain.getRefreshToken())
            .regDateTime(domain.getRegDateTime())
            .roles(domain.getRoles())
            .build();
    }
}
