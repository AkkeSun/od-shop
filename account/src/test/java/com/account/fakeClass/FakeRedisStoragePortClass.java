package com.account.fakeClass;

import com.account.applicaiton.port.out.RedisStoragePort;
import com.account.domain.model.Token;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeRedisStoragePortClass implements RedisStoragePort {

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

    @Override
    public <T> T findData(String key, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> List<T> findDataList(String key, Class<T> clazz) {
        return null;
    }

    @Override
    public void register(String key, String data, long ttl) {

    }

    @Override
    public List<String> getKeys(String input) {
        return null;
    }
}
