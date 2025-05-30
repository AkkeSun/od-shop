package com.account.adapter.out.persistence.jpa;

import com.account.applicaiton.port.out.TokenStoragePort;
import com.account.domain.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
class TokenPersistenceAdapter implements TokenStoragePort {

    private final TokenMapper tokenMapper;
    private final TokenRepository tokenRepository;

    @Override
    public Token findByEmailAndUserAgent(String email, String userAgent) {
        return tokenRepository.findByEmailAndUserAgent(email, userAgent)
            .map(tokenMapper::toDomain)
            .orElse(null);
    }

    @Override
    @Transactional // for test @Transactional
    public void registerToken(Token token) {
        TokenEntity entity = tokenRepository.findByEmail(token.getEmail())
            .map(existingEntity -> {
                existingEntity.updateByDomain(token);
                return existingEntity;
            })
            .orElseGet(() -> tokenMapper.toEntity(token));
        tokenRepository.save(entity);
    }

    @Override
    public void deleteByEmail(String email) {
        tokenRepository.findByEmail(email)
            .ifPresent(tokenRepository::delete);
    }

    @Override
    @Transactional // for test @Transactional
    public void updateToken(Token token) {
        tokenRepository.updateToken(tokenMapper.toEntity(token));
    }
}
