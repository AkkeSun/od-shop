package com.account.fakeClass;

import com.account.applicaiton.port.out.CachePort;
import com.account.domain.model.Token;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeCachePortClass implements CachePort {

    public List<Token> tokenList = new ArrayList<>();

    @Override
    public void registerToken(Token token) {
        tokenList.add(token);
        log.info("FakeCachePortClass registerToken");
    }

    @Override
    public void deleteTokenByEmail(String email) {
        log.info("FakeCachePortClass deleteTokenByEmail");
    }

    @Override
    public Token findTokenByEmailAndUserAgent(String email, String userAgent) {
        List<Token> list = tokenList.stream()
            .filter(token -> token.getEmail().equals(email))
            .filter(token -> token.getUserAgent().equals(userAgent))
            .toList();
        return list.isEmpty() ? null : list.getFirst();
    }
}
