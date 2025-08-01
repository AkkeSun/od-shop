package com.account.adapter.out.persistence.jpa;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

interface RefreshTokenInfoRepository extends JpaRepository<RefreshTokenInfoEntity, Long> {

    Optional<RefreshTokenInfoEntity> findByEmail(String email);

    Optional<RefreshTokenInfoEntity> findByEmailAndUserAgent(String email, String userAgent);

    void deleteByEmail(String email);

    @Modifying
    @Query("update RefreshTokenInfoEntity t "
        + "set t.refreshToken = :#{#token.refreshToken}, t.regDateTime = :#{#token.regDateTime} "
        + "where t.email = :#{#token.email} "
        + "and t.userAgent = :#{#token.userAgent}")
    void updateToken(@Param("token") RefreshTokenInfoEntity token);
}
