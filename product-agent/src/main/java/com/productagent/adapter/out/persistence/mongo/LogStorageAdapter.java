package com.productagent.adapter.out.persistence.mongo;

import com.productagent.application.port.out.LogStoragePort;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class LogStorageAdapter implements LogStoragePort {

    private final MongoTemplate mongoTemplate;

    @Override
    public void register(Object document, String collectionName) {
        if (document instanceof Collection) {
            mongoTemplate.insert((Collection<?>) document, collectionName);
        } else {
            mongoTemplate.insert(document, collectionName);
        }
    }
}
