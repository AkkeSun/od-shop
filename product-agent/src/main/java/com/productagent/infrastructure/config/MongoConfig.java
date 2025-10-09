package com.productagent.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
public class MongoConfig {

    @Bean
    public MongoTemplate mongoTemplate(
        MongoTemplate mongoTemplate,
        MappingMongoConverter converter
    ) {
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return mongoTemplate;
    }
}