package com.order.fakeClass;

import com.order.applicatoin.port.out.RedisStoragePort;
import com.order.infrastructure.util.JsonUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;

public class FakeRedisStoragePort implements RedisStoragePort {

    public Map<String, String> database = new HashMap<>();

    @Override
    public <T> T findData(String key, Class<T> clazz) {
        String redisData = database.get(key);
        if (!StringUtils.hasText(redisData)) {
            return null;
        }
        return JsonUtil.parseJson(redisData, clazz);
    }

    @Override
    public <T> List<T> findDataList(String key, Class<T> clazz) {
        return JsonUtil.parseJsonList(database.get(key), clazz);
    }

    @Override
    public void register(String key, String data, long ttl) {
        database.put(key, data);
    }

    @Override
    public void delete(List<String> keys) {
        keys.forEach(database::remove);
    }

    @Override
    public List<String> getKeys(String input) {
        return List.of();
    }
}
