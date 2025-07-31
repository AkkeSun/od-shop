package com.account.fakeClass;

import com.account.applicaiton.port.out.RoleStoragePort;
import com.account.domain.model.Role;
import java.util.List;

public class FakeRoleStoragePort implements RoleStoragePort {

    @Override
    public List<Role> findAll() {
        return List.of(Role.builder()
            .name("ROLE_CUSTOMER")
            .build());
    }

    @Override
    public void register(Role role) {

    }
}
