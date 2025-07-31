package com.product.adapter.out.persistence.jpa.shard1;

import com.product.application.port.out.AuthorizationStoragePort;
import com.product.domain.model.AuthorizationRule;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(transactionManager = "primaryTransactionManager")
class AuthorizationRuleStorageAdapter implements AuthorizationStoragePort {

    private final AuthorizationRuleRepository repository;

    @Override
    public void register(AuthorizationRule authorization) {
        repository.save(AuthorizationRuleEntity.of(authorization));
    }

    @Override
    public List<AuthorizationRule> findAll() {
        return repository.findAll().stream().map(AuthorizationRuleEntity::toDomain).toList();
    }
}
