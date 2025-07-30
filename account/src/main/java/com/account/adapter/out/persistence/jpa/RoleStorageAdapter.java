package com.account.adapter.out.persistence.jpa;

import com.account.applicaiton.port.out.RoleStoragePort;
import com.account.domain.model.Role;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class RoleStorageAdapter implements RoleStoragePort {

    private final RoleRepository repository;

    @Override
    public List<Role> findAll() {
        return repository.findAll().stream()
            .map(RoleEntity::toDomain)
            .toList();
    }
}
