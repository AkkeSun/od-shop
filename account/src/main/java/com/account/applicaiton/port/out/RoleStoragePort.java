package com.account.applicaiton.port.out;

import com.account.domain.model.Role;
import java.util.List;

public interface RoleStoragePort {

    List<Role> findAll();

    void register(Role role);
}
