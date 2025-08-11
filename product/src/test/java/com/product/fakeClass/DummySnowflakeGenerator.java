package com.product.fakeClass;

import com.product.infrastructure.util.SnowflakeGenerator;

public class DummySnowflakeGenerator implements SnowflakeGenerator {

    @Override
    public long nextId() {
        return 0;
    }

    @Override
    public long nextId(boolean isShard1) {
        return 0;
    }
}
