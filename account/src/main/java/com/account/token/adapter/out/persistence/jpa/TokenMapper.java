package com.account.token.adapter.out.persistence.jpa;

import com.account.token.domain.model.Token;
import org.springframework.stereotype.Component;

@Component
class TokenMapper {

    public Token toDomain(TokenEntity entity) {
        return Token.builder()
            .id(entity.getId())
            .accountId(entity.getAccountId())
            .email(entity.getEmail())
            .userAgent(entity.getUserAgent())
            .refreshToken(entity.getRefreshToken())
            .role(entity.getRole())
            .regDateTime(entity.getRegDateTime())
            .build();
    }

    public TokenEntity toEntity(Token domain) {
        return TokenEntity.builder()
            .id(domain.getId())
            .accountId(domain.getAccountId())
            .email(domain.getEmail())
            .userAgent(domain.getUserAgent())
            .refreshToken(domain.getRefreshToken())
            .regDateTime(domain.getRegDateTime())
            .role(domain.getRole())
            .build();
    }
}
