package com.product.infrastructure.util;

public interface SnowflakeGenerator {

    long nextId();

    long nextId(boolean isShard1);
}
