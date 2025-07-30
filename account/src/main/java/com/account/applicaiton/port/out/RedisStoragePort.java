package com.account.applicaiton.port.out;

import com.account.domain.model.Token;
import java.util.List;

public interface RedisStoragePort {

    void registerToken(Token token);

    void deleteTokenByEmail(String email);

    Token findTokenByEmailAndUserAgent(String email, String userAgent);

    <T> T findData(String key, Class<T> clazz);

    <T> List<T> findDataList(String key, Class<T> clazz);

    void register(String key, String data, long ttl);

    List<String> getKeys(String input);

}
