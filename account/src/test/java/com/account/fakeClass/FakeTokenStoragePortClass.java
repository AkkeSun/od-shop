package com.account.fakeClass;

import com.account.applicaiton.port.out.TokenStoragePort;
import com.account.domain.model.Token;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeTokenStoragePortClass implements TokenStoragePort {

    public List<Token> tokenList = new ArrayList<>();

    @Override
    public void registerToken(Token token) {
        tokenList.add(token);
        log.info("FakeTokenStoragePortClass registerToken");
    }

    @Override
    public void deleteByEmail(String email) {
        log.info("FakeTokenStoragePortClass deleteByEmail");
    }

    @Override
    public Token findByEmailAndUserAgent(String email, String userAgent) {
        List<Token> list = tokenList.stream()
            .filter(token -> token.getEmail().equals(email))
            .filter(token -> token.getUserAgent().equals(userAgent))
            .toList();
        return list.isEmpty() ? null : list.getFirst();
    }

    @Override
    public void updateToken(Token tokenCache) {
        log.info("FakeTokenStoragePortClass updateToken");
    }
}
