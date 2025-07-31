package com.product.adapter.out.persistence.jpa.shard1;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

interface AuthorizationRuleRepository extends JpaRepository<AuthorizationRuleEntity, Long> {

    @Query("select a from AuthorizationRuleEntity a order by a.sortOrder")
    List<AuthorizationRuleEntity> findAll();

}
