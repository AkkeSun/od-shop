package com.accountagent.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {

    @Modifying
    @Query("delete from TokenEntity token where token.regDateTime between :start and :end")
    void deleteByRegDateTimeBetween(String start, String end);
}
