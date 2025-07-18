package com.productagent.application.port.out;

public interface LogStoragePort {

    void register(Object document, String collectionName);
}
