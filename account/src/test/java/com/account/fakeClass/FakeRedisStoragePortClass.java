package com.account.fakeClass;

import com.account.applicaiton.port.out.RedisStoragePort;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeRedisStoragePortClass implements RedisStoragePort {

    public Map<String, String> redisData = new HashMap<>();

    @Override
    public <T> T findData(String key, Class<T> clazz) {
        String s = redisData.get(key);
        if (s != null) {
            return JsonUtil.parseJson(s, clazz);
        }
        return null;
    }

    @Override
    public <T> List<T> findDataList(String key, Class<T> clazz) {
        String s = redisData.get(key);
        if (s != null) {
            return JsonUtil.parseJsonList(s, clazz);
        }
        return new ArrayList<>();
    }

    @Override
    public void register(String key, String data, long ttl) {
        redisData.put(key, data);
    }

    @Override
    public void delete(List<String> keys) {
        log.info("FakeCachePortClass deleteTokenByEmail");
        for (String s : keys) {
            redisData.remove(s);
        }
    }

    @Override
    public List<String> getKeys(String input) {
        return new ArrayList<>();
    }
}
