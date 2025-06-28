package com.product.application.port.out;

import java.util.List;

public interface RedisStoragePort {

    <T> T findData(String key, Class<T> clazz);

    <T> List<T> findDataList(String key, Class<T> clazz);

    void register(String key, String data, long ttl);
}
