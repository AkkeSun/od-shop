package com.accountagent.adapter.out.persistence.redis;

import static com.common.infrastructure.util.JsonUtil.parseJson;

import com.accountagent.application.port.out.RedisStoragePort;
import com.accountagent.domain.model.AccountHistory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisStorageAdapter implements RedisStoragePort {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Map<String, AccountHistory> findAllAccountHistory() {
        Map<String, AccountHistory> historyMap = new HashMap<>();
        for (String key : scanByPattern("account-history*")) {
            String s = redisTemplate.opsForValue().get(key);
            final String s1 = redisTemplate.opsForValue().get(key);
            historyMap.put(key,
                parseJson(redisTemplate.opsForValue().get(key), AccountHistory.class));
        }

        return historyMap;
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    private List<String> scanByPattern(String pattern) {
        RedisConnection redisConnection =
            redisTemplate.getConnectionFactory().getConnection();
        ScanOptions scanOptions =
            ScanOptions.scanOptions().count(50).match(pattern).build();
        Cursor cursor = redisConnection.scan(scanOptions);

        List<String> redisKeys = new ArrayList<>();
        while (cursor.hasNext()) {
            String key = new String((byte[]) cursor.next());
            redisKeys.add(key);
        }

        return redisKeys;
    }
}
