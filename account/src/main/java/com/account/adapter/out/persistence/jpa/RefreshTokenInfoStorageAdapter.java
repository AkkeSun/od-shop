package com.account.adapter.out.persistence.jpa;

import com.account.applicaiton.port.out.RefreshTokenInfoStoragePort;
import com.account.domain.model.RefreshTokenInfo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RefreshTokenInfoStorageAdapter implements RefreshTokenInfoStoragePort {

    private final RefreshTokenInfoMapper tokenMapper;
    private final RefreshTokenInfoRepository tokenRepository;

    @Override
    public RefreshTokenInfo findByEmailAndUserAgent(String email, String userAgent) {
        return tokenRepository.findByEmailAndUserAgent(email, userAgent)
            .map(tokenMapper::toDomain)
            .orElse(null);
    }

    @Override
    public void registerToken(RefreshTokenInfo token) {
        RefreshTokenInfoEntity entity = tokenRepository.findByEmail(token.getEmail())
            .map(existingEntity -> {
                existingEntity.updateByDomain(token);
                return existingEntity;
            })
            .orElseGet(() -> tokenMapper.toEntity(token));
        tokenRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteByEmail(String email) {
        tokenRepository.deleteByEmail(email);
    }

    @Override
    @Transactional
    public void updateToken(RefreshTokenInfo token) {
        tokenRepository.updateToken(tokenMapper.toEntity(token));
    }
}
