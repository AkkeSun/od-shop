package com.product.application.port.out;

import com.product.domain.model.AuthorizationRule;
import java.util.List;

public interface AuthorizationStoragePort {

    void register(AuthorizationRule authorization);

    List<AuthorizationRule> findAll();
}
