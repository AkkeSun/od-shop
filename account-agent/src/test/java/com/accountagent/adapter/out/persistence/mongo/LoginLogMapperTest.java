package com.accountagent.adapter.out.persistence.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import com.accountagent.domain.model.LoginLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("LoginLogMapper 테스트")
class LoginLogMapperTest {

    @Test
    @DisplayName("[toDocument] LoginLog를 LoginLogDocument로 변환한다")
    void toDocument() {
        // given
        LoginLog loginLog = LoginLog.builder()
            .accountId(1L)
            .email("test@example.com")
            .loginDateTime("2025-10-25 10:00:00")
            .build();

        // when
        LoginLogDocument document = LoginLogMapper.toDocument(loginLog);

        // then
        assertThat(document).isNotNull();
        assertThat(document.accountId()).isEqualTo(1L);
        assertThat(document.email()).isEqualTo("test@example.com");
        assertThat(document.loginDateTime()).isEqualTo("2025-10-25 10:00:00");
    }

    @Test
    @DisplayName("[toDocument] 모든 필드가 null인 LoginLog도 변환 가능하다")
    void toDocument_nullFields() {
        // given
        LoginLog loginLog = LoginLog.builder()
            .accountId(null)
            .email(null)
            .loginDateTime(null)
            .build();

        // when
        LoginLogDocument document = LoginLogMapper.toDocument(loginLog);

        // then
        assertThat(document).isNotNull();
        assertThat(document.accountId()).isNull();
        assertThat(document.email()).isNull();
        assertThat(document.loginDateTime()).isNull();
    }

    @Test
    @DisplayName("[toDocument] 여러 사용자의 LoginLog를 변환한다")
    void toDocument_multipleUsers() {
        // given
        LoginLog loginLog1 = LoginLog.builder()
            .accountId(1L)
            .email("user1@example.com")
            .loginDateTime("2025-10-25 10:00:00")
            .build();

        LoginLog loginLog2 = LoginLog.builder()
            .accountId(2L)
            .email("user2@example.com")
            .loginDateTime("2025-10-25 11:00:00")
            .build();

        // when
        LoginLogDocument document1 = LoginLogMapper.toDocument(loginLog1);
        LoginLogDocument document2 = LoginLogMapper.toDocument(loginLog2);

        // then
        assertThat(document1.accountId()).isEqualTo(1L);
        assertThat(document1.email()).isEqualTo("user1@example.com");
        assertThat(document2.accountId()).isEqualTo(2L);
        assertThat(document2.email()).isEqualTo("user2@example.com");
    }

    @Test
    @DisplayName("[toDocument] 특수문자가 포함된 이메일도 변환 가능하다")
    void toDocument_specialCharactersInEmail() {
        // given
        LoginLog loginLog = LoginLog.builder()
            .accountId(1L)
            .email("test+special@example.co.kr")
            .loginDateTime("2025-10-25 10:00:00")
            .build();

        // when
        LoginLogDocument document = LoginLogMapper.toDocument(loginLog);

        // then
        assertThat(document.email()).isEqualTo("test+special@example.co.kr");
    }
}