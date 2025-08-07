package com.account.fakeClass;

import com.account.domain.model.RefreshTokenInfo;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeTokenStoragePortClass implements RefreshTokenInfoStoragePort {

    public List<RefreshTokenInfo> tokenList = new ArrayList<>();

    @Override
    public void registerToken(RefreshTokenInfo token) {
        tokenList.add(token);
        log.info("FakeTokenStoragePortClass registerToken");
    }

    @Override
    public void deleteByEmail(String email) {
        log.info("FakeTokenStoragePortClass deleteByEmail");
    }

    @Override
    public RefreshTokenInfo findByEmailAndUserAgent(String email, String userAgent) {
        List<RefreshTokenInfo> list = tokenList.stream()
            .filter(token -> token.getEmail().equals(email))
            .filter(token -> token.getUserAgent().equals(userAgent))
            .toList();
        return list.isEmpty() ? null : list.getFirst();
    }

    @Override
    public void updateToken(RefreshTokenInfo tokenCache) {
        log.info("FakeTokenStoragePortClass updateToken");
    }
}
