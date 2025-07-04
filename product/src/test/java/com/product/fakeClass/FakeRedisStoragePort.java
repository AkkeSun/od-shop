package com.product.fakeClass;

import com.product.application.port.out.RedisStoragePort;
import com.product.infrastructure.util.JsonUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeRedisStoragePort implements RedisStoragePort {

    public Map<String, String> database = new HashMap<>();

    @Override
    public <T> T findData(String key, Class<T> clazz) {
        String data = database.get(key);
        return data == null ? null : JsonUtil.parseJson(data, clazz);
    }

    @Override
    public <T> List<T> findDataList(String key, Class<T> clazz) {
        String data = database.get(key);
        return data == null ? Collections.emptyList() : JsonUtil.parseJsonList(data, clazz);
    }

    @Override
    public void register(String key, String data, long ttl) {
        database.put(key, data);
    }
}
