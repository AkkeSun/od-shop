package com.account.adapter.out.persistence.jpa;

import com.account.IntegrationTestSupport;
import com.account.domain.model.RefreshTokenInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RefreshTokenInfoStorageAdapterTest extends IntegrationTestSupport {

    @Autowired
    private RefreshTokenInfoStorageAdapter adapter;
    @Autowired
    private RefreshTokenInfoRepository tokenRepository;

    @BeforeEach
    @AfterEach
    void tearDown() {
        tokenRepository.deleteAll();
    }

    @Nested
    @DisplayName("[findByEmailAndUserAgent] 사용자 이메일과 UserAgent 로 토큰을 조회하는 메소드")
    class Describe_findByEmailAndUserAgent {

        @Test
        @DisplayName("[success] 조회된 토큰이 있다면 토큰을 응답한다.")
        void success() {
            // given
            String email = "email";
            String userAgent = "userAgent";
            RefreshTokenInfoEntity entity = RefreshTokenInfoEntity.builder()
                .email(email)
                .userAgent(userAgent)
                .refreshToken("refreshToken")
                .roles("role")
                .accountId(1L)
                .build();
            tokenRepository.save(entity);

            // when
            RefreshTokenInfo token = adapter.findByEmailAndUserAgent(email, userAgent);

            // then
            assert token.getEmail().equals(email);
            assert token.getUserAgent().equals(userAgent);
            assert token.getRefreshToken().equals("refreshToken");
            assert token.getRoles().equals("role");
            assert token.getAccountId().equals(1L);
        }

        @Test
        @DisplayName("[success] 조회된 토큰이 없다면 null 을 응답한다.")
        void success2() {
            // given
            String email = "error";
            String userAgent = "error";

            // when
            RefreshTokenInfo token = adapter.findByEmailAndUserAgent(email, userAgent);

            // then
            assert token == null;
        }
    }

    @Nested
    @DisplayName("[registerToken] 토큰을 등록하는 메소드")
    class Describe_registerToken {

        @Test
        @DisplayName("[success] 저장된 토큰이 없다면 신규 토큰을 저장한다.")
        void success() {
            // given
            RefreshTokenInfo token = RefreshTokenInfo.builder()
                .email("email")
                .userAgent("userAgent")
                .refreshToken("refreshToken")
                .roles("role")
                .accountId(1L)
                .build();

            // when
            adapter.registerToken(token);

            // then
            RefreshTokenInfoEntity entity = tokenRepository.findByEmail("email").get();
            assert entity.getEmail().equals("email");
            assert entity.getUserAgent().equals("userAgent");
            assert entity.getRefreshToken().equals("refreshToken");
            assert entity.getRoles().equals("role");
            assert entity.getAccountId().equals(1L);
        }


        @Test
        @DisplayName("[success] 저장된 토큰이 있다면 기존 토큰을 수정하여 저장한다.")
        void success2() {
            // given
            RefreshTokenInfoEntity entity = RefreshTokenInfoEntity.builder()
                .email("email")
                .userAgent("userAgent")
                .refreshToken("refreshToken")
                .roles("role")
                .accountId(1L)
                .regDateTime("regDateTime")
                .build();
            tokenRepository.save(entity);
            RefreshTokenInfo token = RefreshTokenInfo.builder()
                .email("email")
                .userAgent("newUserAgent")
                .refreshToken("newRefreshToken")
                .regDateTime("newRegDateTime")
                .roles("role")
                .accountId(1L)
                .build();

            // when
            adapter.registerToken(token);
            RefreshTokenInfoEntity result = tokenRepository.findByEmail("email").get();

            // then
            assert result.getEmail().equals(token.getEmail());
            assert result.getUserAgent().equals(token.getUserAgent());
            assert result.getRefreshToken().equals(token.getRefreshToken());
            assert result.getRoles().equals(token.getRoles());
            assert result.getRegDateTime().equals(token.getRegDateTime());
            assert result.getAccountId().equals(1L);
        }
    }

    @Nested
    @DisplayName("[deleteByEmail] 사용자 이메일로 토큰을 삭제하는 메소드")
    class Describe_deleteByEmail {

        @Test
        @DisplayName("[success] 삭제할 토큰이 있다면 토큰을 삭제한다.")
        void success() {
            // given
            RefreshTokenInfoEntity entity = RefreshTokenInfoEntity.builder()
                .email("email")
                .userAgent("userAgent")
                .refreshToken("refreshToken")
                .roles("role")
                .accountId(1L)
                .build();
            tokenRepository.save(entity);

            // when
            adapter.deleteByEmail("email");

            // then
            assert tokenRepository.findByEmail("email").isEmpty();
        }
    }

    @Nested
    @DisplayName("[updateToken] 토큰을 수정하는 메소드")
    class Describe_updateToken {

        @Test
        @DisplayName("[success] 토큰을 수정한다.")
        void success() {
            // given
            RefreshTokenInfo token1Domain = RefreshTokenInfo.builder()
                .email("updateToken.success.email")
                .userAgent("updateToken.success.userAgent")
                .refreshToken("updateToken.success.refreshToken")
                .regDateTime("updateToken.success.regDateTime")
                .roles("ROLE_CUSTOMER")
                .build();
            adapter.registerToken(token1Domain);
            RefreshTokenInfo token1Domain2 = RefreshTokenInfo.builder()
                .email("updateToken.success.email")
                .userAgent("updateToken.success.userAgent")
                .refreshToken("updateRefreshToken")
                .regDateTime("updateRegDateTime")
                .roles("ROLE_CUSTOMER")
                .build();
            // when

            adapter.updateToken(token1Domain2);
            RefreshTokenInfoEntity result = tokenRepository.findByEmailAndUserAgent(
                token1Domain.getEmail(),
                token1Domain.getUserAgent()).get();

            // then
            assert result.getUserAgent().equals(token1Domain2.getUserAgent());
            assert result.getRefreshToken().equals(token1Domain2.getRefreshToken());
            assert result.getRegDateTime().equals(token1Domain2.getRegDateTime());
        }
    }
}