package com.common.infrastructure.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SnowflakeGeneratorImpl implements SnowflakeGenerator {

    @Value("${service-constant.snowflake.dataCenterId}")
    private long datacenterId;

    @Value("${service-constant.snowflake.workerId}")
    private long workerId;

    private final long epoch = 1288834974657L; // 기준 시점 (2010-11-04 01:42:54 UTC)

    private final long datacenterIdBits = 5L;

    private final long workerIdBits = 5L;

    private final long sequenceBits = 12L;

    private final long maxDatacenterId = ~(-1L << datacenterIdBits); // 최대 31

    private final long maxWorkerId = ~(-1L << workerIdBits);         // 최대 31

    private final long sequenceMask = ~(-1L << sequenceBits);        // 최대 4095

    private long sequence = 0L;

    private long lastTimestamp = -1L;

    public synchronized long nextId() {
        if (datacenterId > maxDatacenterId || datacenterId < 0) {
            throw new IllegalArgumentException("Invalid Data Center ID");
        }
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException("Invalid Worker ID");
        }

        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                currentTimestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;
        return ((currentTimestamp - epoch) << 22) | (datacenterId << 17) | (workerId << 12)
            | sequence;
    }

    @Override
    public long nextId(boolean isShard1) {
        long id = nextId();
        if (isShard1 && ShardKeyUtil.isShard1(id)) {
            return id;
        }
        if (!isShard1 && !ShardKeyUtil.isShard1(id)) {
            return id;
        }
        return nextId(isShard1);
    }

    private long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}
