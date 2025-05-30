package com.accountagent.adapter.out.persistence.jpa;

import com.accountagent.application.port.out.TokenStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class TokenStoragePersistenceAdapter implements TokenStoragePort {


    private final TokenRepository tokenRepository;

    @Override
    public void deleteByRegDateTimeBetween(String start, String end) {
        tokenRepository.deleteByRegDateTimeBetween(start, end);
    }
}
