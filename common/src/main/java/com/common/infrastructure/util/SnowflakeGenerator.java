package com.common.infrastructure.util;

public interface SnowflakeGenerator {

    long nextId();

    long nextId(boolean isShard1);
}
