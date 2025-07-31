package com.account.infrastructure.util;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.infrastructure.exception.CustomAuthenticationException;
import com.account.infrastructure.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtUtilTest {

    JwtUtil jwtUtil;

    @BeforeEach
    public void init() {
        jwtUtil = new JwtUtilImpl();
        ReflectionTestUtils.setField(jwtUtil, "secretKey", "hello-jwt");
        ReflectionTestUtils.setField(jwtUtil, "tokenValidTime", 5000);
        ReflectionTestUtils.setField(jwtUtil, "refreshTokenValidTime", 10000);
    }

    @Nested
    @DisplayName("[createAccessToken] 사용자 정보를 기반으로 인증 토큰을 생성하여 응답하는 메소드")
    class Describe_createAccessToken {

        @Test
        @DisplayName("[success] 정상적으로 인증 토큰이 생성되는지 확인한다.")
        void success() {
            // given
            Account account = Account.builder()
                .id(1L)
                .email("od@gmail.com")
                .roles(List.of(Role.builder().id(1L).name("ROLE_CUSTOMER").build()))
                .build();

            // when
            String accessToken = jwtUtil.createAccessToken(account);
            String email = jwtUtil.getEmail(accessToken);
            boolean exceptExpiration = jwtUtil.validateTokenExceptExpiration(accessToken);

            // then
            assert email.equals(account.getEmail());
            assert exceptExpiration;
        }
    }

    @Nested
    @DisplayName("[createRefreshToken] 사용자 이메일을 기반으로 리프레시 토큰을 생성하여 응답하는 메소드")
    class Describe_createRefreshToken {

        @Test
        @DisplayName("[success] 정상적으로 리프레시 토큰이 생성되는지 확인한다.")
        void success() {
            // given
            String email = "od@test.gmail.com";

            // when
            String refreshToken = jwtUtil.createRefreshToken(email);
            String emailFromToken = jwtUtil.getEmail(refreshToken);

            // then
            assert emailFromToken.equals(email);
        }
    }

    @Nested
    @DisplayName("[validateTokenExceptExpiration] 토큰 유효성을 검사하는 메소드")
    class Describe_validateTokenExceptExpiration {

        @Test
        @DisplayName("[success] 유효한 토큰인 경우 true 를 응답 한다.")
        void success() {
            // given
            String token = jwtUtil.createRefreshToken("test2");

            // when
            boolean result = jwtUtil.validateTokenExceptExpiration(token);

            // then
            assert result;
        }

        @Test
        @DisplayName("[success] 유효한 토큰이 아닌 경우 false 를 응답 한다.")
        void success2() {
            // given
            String token = "unknown token";

            // when
            boolean result = jwtUtil.validateTokenExceptExpiration(token);

            // then
            assert !result;
        }
    }


    @Nested
    @DisplayName("[getEmail] 토큰에서 이메일을 추출하여 응답하는 메소드")
    class Describe_getEmail {

        @Test
        @DisplayName("[success] 정상적으로 토큰에서 이메일을 추출하여 응답하는지 확인한다.")
        void success() {
            // given
            String token = jwtUtil.createRefreshToken("test");

            // when
            String email = jwtUtil.getEmail(token);

            // then
            assert email.equals("test");
        }

        @Test
        @DisplayName("[error] 토큰에서 이메일을 추출하는 중 예외 발생시 CustomAuthenticationException 을 응답한다.")
        void error() {
            // given
            String token = "error";

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class, () -> jwtUtil.getEmail(token));

            // then
            assert exception.getErrorCode().equals(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    @Nested
    @DisplayName("[getAccountId] 토큰에서 사용자 아이디를 추출하여 응답하는 메소드")
    class Describe_getAccountId {

        @Test
        @DisplayName("[success] 정상적으로 토큰에서 사용자 아이디를 추출하여 응답하는지 확인한다.")
        void success() {
            // given
            Account account = Account.builder()
                .id(1L)
                .email("1234")
                .roles(List.of(Role.builder().id(1L).name("ROLE_CUSTOMER").build()))
                .build();
            String token = jwtUtil.createAccessToken(account);

            // when
            Long accountId = jwtUtil.getAccountId(token);

            // then
            assert accountId.equals(1L);
        }

        @Test
        @DisplayName("[error] 토큰에서 사용자 아이디를 추출하는 중 예외 발생시 CustomAuthenticationException 을 응답한다.")
        void error() {
            // given
            String token = "error";

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class, () -> jwtUtil.getAccountId(token));

            // then
            assert exception.getErrorCode().equals(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    @Nested
    @DisplayName("[getClaims] 토큰에서 클레임을 추출하여 응답하는 메소드")
    class Describe_getClaims {

        @Test
        @DisplayName("[success] 정상적으로 토큰에서 클레임을 추출하여 응답하는지 확인한다.")
        void success() {
            // given
            Account account = Account.builder()
                .id(1L)
                .email("1234")
                .roles(List.of(Role.builder().id(1L).name("ROLE_CUSTOMER").build()))
                .build();
            String token = jwtUtil.createAccessToken(account);

            // when
            Claims claims = jwtUtil.getClaims(token);

            // then
            assert claims.getSubject().equals("1234");
            assert claims.get("accountId").equals(1);
            assert claims.get("roles").equals("ROLE_CUSTOMER");
        }

        @Test
        @DisplayName("[error] 토큰에서 클레임을 추출하는 중 예외 발생시 CustomAuthenticationException 을 응답한다.")
        void error() {
            // given
            String token = "error";

            // when
            CustomAuthenticationException exception = assertThrows(
                CustomAuthenticationException.class, () -> jwtUtil.getClaims(token));

            // then
            assert exception.getErrorCode().equals(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }
}