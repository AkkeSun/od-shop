package com.order.domain.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AccountTest {

    @Nested
    @DisplayName("[of] Claims 객체를 Account 객체로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] Claims 객체를 Account 객체로 변환하는 경우 성공적으로 변환된다.")
        void success() {
            // given
            Claims claims = Jwts.claims().setSubject("test@gmail.com");
            claims.put("accountId", 1L);
            claims.put("roles", "USER");

            // when
            Account account = Account.of(claims);

            // then
            assert account.id().equals(1L);
            assert account.roles().contains("USER");
        }
    }
}