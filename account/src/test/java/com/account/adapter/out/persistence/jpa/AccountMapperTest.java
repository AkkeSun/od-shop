package com.account.adapter.out.persistence.jpa;

import com.account.IntegrationTestSupport;
import com.account.domain.model.Account;
import com.account.domain.model.Role;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AccountMapperTest extends IntegrationTestSupport {

    @Autowired
    AccountMapper mapper;

    @Nested
    @DisplayName("[toDomain] entity를 domain 으로 변환하는 메소드")
    class Describe_toDomain {

        @Test
        @DisplayName("[success] entity를 정상적으로 domain으로 변환하는지 확인한다.")
        void success() {
            // given
            AccountEntity entity = AccountEntity.builder()
                .id(1234L)
                .email("email")
                .password("password")
                .username("username")
                .userTel("userTel")
                .address("address")
                .role(Role.ROLE_SELLER)
                .regDateTime(LocalDateTime.now())
                .regDate("regDate")
                .build();

            // when
            Account domain = mapper.toDomain(entity);

            // then
            assert domain.getId().equals(entity.getId());
            assert domain.getEmail().equals(entity.getEmail());
            assert domain.getPassword().equals(entity.getPassword());
            assert domain.getUsername().equals(entity.getUsername());
            assert domain.getUserTel().equals(entity.getUserTel());
            assert domain.getAddress().equals(entity.getAddress());
            assert domain.getRole().equals(entity.getRole());
            assert domain.getRegDateTime().equals(entity.getRegDateTime());
            assert domain.getRegDate().equals(entity.getRegDate());
        }
    }

    @Nested
    @DisplayName("[toEntity] domain을 entity로 변환하는 메소드")
    class Describe_toEntity {

        @Test
        @DisplayName("[success] domain을 정상적으로 entity로 변환하는지 확인한다.")
        void success() {
            // given
            Account domain = Account.builder()
                .id(1234L)
                .email("email")
                .password("password")
                .username("username")
                .userTel("userTel")
                .address("address")
                .role(Role.ROLE_SELLER)
                .regDateTime(LocalDateTime.now())
                .regDate("regDate")
                .build();

            // when
            AccountEntity entity = mapper.toEntity(domain);

            // then
            assert entity.getId().equals(domain.getId());
            assert entity.getEmail().equals(domain.getEmail());
            assert entity.getPassword().equals(aesUtil.encryptText(domain.getPassword()));
            assert entity.getUsername().equals(domain.getUsername());
            assert entity.getUserTel().equals(domain.getUserTel());
            assert entity.getAddress().equals(domain.getAddress());
            assert entity.getRole().equals(domain.getRole());
            assert entity.getRegDateTime().equals(domain.getRegDateTime());
            assert entity.getRegDate().equals(domain.getRegDate());
        }
    }
}