package com.account.adapter.out.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.account.IntegrationTestSupport;
import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.infrastructure.exception.CustomNotFoundException;
import com.account.infrastructure.exception.ErrorCode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

class AccountStorageAdapterTest extends IntegrationTestSupport {

    @Autowired
    AccountStorageAdapter adapter;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }

    @BeforeEach
    void setup() {
        circuitBreakerRegistry.circuitBreaker("kafka").reset();
    }

    @Nested
    @DisplayName("[findById] 사용자 아이디로 사용자 정보를 조회하는 메소드")
    class Describe_findById {

        @Test
        @DisplayName("[success] 조회된 사용자 정보가 있는 경우 사용자 정보를 반환한다.")
        void success() {
            // given
            AccountEntity entity = AccountEntity.builder()
                .email("email")
                .password("password")
                .username("username")
                .userTel("userTel")
                .address("address")
                .roles(Set.of(RoleEntity.builder().id(1L).name("ROLE_CUSTOMER").build()))
                .regDateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                .regDate("20210101")
                .build();
            AccountEntity savedEntity = accountRepository.save(entity);

            // when
            Account account = adapter.findById(savedEntity.getId());

            // then
            assert account.getId().equals(savedEntity.getId());
            assert account.getEmail().equals(savedEntity.getEmail());
            assert account.getPassword().equals(savedEntity.getPassword());
            assert account.getUsername().equals(savedEntity.getUsername());
            assert account.getUserTel().equals(savedEntity.getUserTel());
            assert account.getAddress().equals(savedEntity.getAddress());
            assert account.getRegDateTime().equals(savedEntity.getRegDateTime());
            assert account.getRegDate().equals(savedEntity.getRegDate());
        }

        @Test
        @DisplayName("[error] 조회된 사용자 정보가 없는 경우 CustomNotFoundException 을 응답한다.")
        void error() {
            // given
            Long id = -1L;

            // when
            CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> adapter.findById(id));

            // then
            assert exception.getErrorCode().equals(ErrorCode.DoesNotExist_ACCOUNT_INFO);
        }
    }

    @Nested
    @DisplayName("[findByEmailAndPassword] 사용자 이메일과 패스워드로 사용자 정보를 조회하는 메소드")
    class Describe_findByEmailAndPassword {

        @Test
        @DisplayName("[success] 조회된 사용자 정보가 있는 경우 사용자 정보를 반환한다.")
        void success() {
            // given
            AccountEntity entity = AccountEntity.builder()
                .email("email")
                .password(passwordEncoder.encode("password"))
                .username("username")
                .userTel("userTel")
                .address("address")
                .roles(Set.of(RoleEntity.builder().id(1L).name("ROLE_CUSTOMER").build()))
                .regDateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                .regDate("20210101")
                .build();
            AccountEntity savedEntity = accountRepository.save(entity);

            // when
            Account account = adapter.findByEmailAndPassword(savedEntity.getEmail(),
                "password");

            // then
            assert account.getId().equals(savedEntity.getId());
            assert account.getEmail().equals(savedEntity.getEmail());
            assert account.getUsername().equals(savedEntity.getUsername());
            assert account.getUserTel().equals(savedEntity.getUserTel());
            assert account.getAddress().equals(savedEntity.getAddress());
            assert account.getRegDateTime().equals(savedEntity.getRegDateTime());
            assert account.getRegDate().equals(savedEntity.getRegDate());
        }

        @Test
        @DisplayName("[error] 조회된 사용자 정보가 없는 경우 CustomNotFoundException 을 응답한다.")
        void error() {
            // given
            String email = "email";
            String password = "error";

            // when
            CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> adapter.findByEmailAndPassword(email, password));

            // then
            assert exception.getErrorCode().equals(ErrorCode.DoesNotExist_ACCOUNT_INFO);
        }
    }

    @Nested
    @DisplayName("[existsByEmail] 사용자 이메일로 조회된 사용자 정보가 있는지 확인하는 메소드")
    class Describe_existsByEmail {

        @Test
        @DisplayName("[success] 조회된 사용자 정보가 있는 경우 true 를 반환한다.")
        void success() {
            // given
            AccountEntity entity = AccountEntity.builder()
                .email("email")
                .password("password")
                .username("username")
                .userTel("userTel")
                .address("address")
                .roles(Set.of(RoleEntity.builder().id(1L).name("ROLE_CUSTOMER").build()))
                .regDateTime(LocalDateTime.now())
                .regDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build();
            accountRepository.save(entity);

            // when
            boolean exists = adapter.existsByEmail(entity.getEmail());

            // then
            assert exists;
        }

        @Test
        @DisplayName("[error] 조회된 사용자 정보가 없는 경우 false 를 반환한다.")
        void error() {
            // given
            String email = "error";

            // when
            boolean exists = adapter.existsByEmail(email);

            // then
            assert !exists;
        }
    }

    @Nested
    @DisplayName("[register] 사용자 정보를 등록하는 메소드")
    class Describe_register {

        @Test
        @DisplayName("[success] 사용자 정보를 등록한다.")
        void success() {
            // given
            Account account = Account.builder()
                .email("email")
                .password("password")
                .username("username")
                .userTel("userTel")
                .address("address")
                .roles(List.of(Role.builder().id(1L).name("ROLE_CUSTOMER").build()))
                .regDateTime(LocalDateTime.now())
                .regDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build();

            // when
            Account savedAccount = adapter.register(account);

            // then
            assert savedAccount.getId() != null;
            assert savedAccount.getEmail().equals(account.getEmail());
            assert savedAccount.getUsername().equals(account.getUsername());
            assert savedAccount.getUserTel().equals(account.getUserTel());
            assert savedAccount.getAddress().equals(account.getAddress());
            assert savedAccount.getRegDateTime().equals(account.getRegDateTime());
            assert savedAccount.getRegDate().equals(account.getRegDate());
        }
    }

    @Nested
    @DisplayName("[update] 사용자 정보를 수정하는 메소드")
    class Describe_update {

        @Test
        @DisplayName("[success] 사용자 정보를 수정한다.")
        void success() {
            // given
            AccountEntity entity = AccountEntity.builder()
                .email("email")
                .password("password")
                .username("username")
                .userTel("userTel")
                .address("address")
                .roles(Set.of(RoleEntity.builder().id(1L).name("ROLE_CUSTOMER").build()))
                .regDateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                .regDate("20210101")
                .build();
            AccountEntity savedEntity = accountRepository.save(entity);

            Account account = Account.builder()
                .id(savedEntity.getId())
                .email(savedEntity.getEmail())
                .password("updatePassword")
                .username("updateUsername")
                .userTel("updateUserTel")
                .address("updateAddress")
                .roles(List.of(Role.builder().id(1L).name("ROLE_CUSTOMER").build()))
                .regDateTime(LocalDateTime.of(2021, 1, 1, 0, 0))
                .regDate("20210101")
                .build();

            // when
            adapter.update(account);
            Account updatedAccount = adapter.findById(savedEntity.getId());

            // then
            assert updatedAccount.getId().equals(account.getId());
            assert updatedAccount.getEmail().equals(account.getEmail());
            assert updatedAccount.getUsername().equals(account.getUsername());
            assert updatedAccount.getUserTel().equals(account.getUserTel());
            assert updatedAccount.getAddress().equals(account.getAddress());
            assert updatedAccount.getRegDateTime().equals(account.getRegDateTime());
            assert updatedAccount.getRegDate().equals(account.getRegDate());
        }
    }

    @Nested
    @DisplayName("[deleteById] 사용자 아이디로 사용자 정보를 삭제하는 메소드")
    class Describe_deleteById {

        @Test
        @DisplayName("[success] 사용자 정보를 삭제한다.")
        void success() {
            // given
            AccountEntity entity = AccountEntity.builder()
                .email("email")
                .password("password")
                .username("username")
                .userTel("userTel")
                .address("address")
                .roles(Set.of(RoleEntity.builder().id(1L).name("ROLE_CUSTOMER").build()))
                .regDateTime(LocalDateTime.now())
                .regDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .build();
            AccountEntity savedEntity = accountRepository.save(entity);

            // when
            adapter.deleteById(savedEntity.getId());

            // then
            CustomNotFoundException exception = assertThrows(CustomNotFoundException.class,
                () -> adapter.findById(savedEntity.getId()));
            assert exception.getErrorCode().equals(ErrorCode.DoesNotExist_ACCOUNT_INFO);
        }
    }
}