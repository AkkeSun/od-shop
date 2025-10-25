package com.accountagent.adapter.out.persistence.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import com.accountagent.domain.model.DlqLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("DlqLogMapper 테스트")
class DlqLogMapperTest {

    @Test
    @DisplayName("[toDocument] DlqLog를 DlqLogDocument로 변환한다")
    void toDocument() {
        // given
        DlqLog dlqLog = DlqLog.builder()
            .topic("account-history")
            .payload("{\"accountId\":1,\"type\":\"CREATE\"}")
            .regDateTime("2025-10-25 10:00:00")
            .build();

        // when
        DlqLogDocument document = DlqLogMapper.toDocument(dlqLog);

        // then
        assertThat(document).isNotNull();
        assertThat(document.topic()).isEqualTo("account-history");
        assertThat(document.payload()).isEqualTo("{\"accountId\":1,\"type\":\"CREATE\"}");
        assertThat(document.regDateTime()).isEqualTo("2025-10-25 10:00:00");
    }

    @Test
    @DisplayName("[toDocument] 모든 필드가 null인 DlqLog도 변환 가능하다")
    void toDocument_nullFields() {
        // given
        DlqLog dlqLog = DlqLog.builder()
            .topic(null)
            .payload(null)
            .regDateTime(null)
            .build();

        // when
        DlqLogDocument document = DlqLogMapper.toDocument(dlqLog);

        // then
        assertThat(document).isNotNull();
        assertThat(document.topic()).isNull();
        assertThat(document.payload()).isNull();
        assertThat(document.regDateTime()).isNull();
    }

    @Test
    @DisplayName("[toDocument] 다양한 토픽의 DlqLog를 변환한다")
    void toDocument_variousTopics() {
        // given
        DlqLog accountHistoryDlq = DlqLog.builder()
            .topic("account-history")
            .payload("{\"accountId\":1}")
            .regDateTime("2025-10-25 10:00:00")
            .build();

        DlqLog loginLogDlq = DlqLog.builder()
            .topic("account-login")
            .payload("{\"email\":\"test@example.com\"}")
            .regDateTime("2025-10-25 11:00:00")
            .build();

        // when
        DlqLogDocument doc1 = DlqLogMapper.toDocument(accountHistoryDlq);
        DlqLogDocument doc2 = DlqLogMapper.toDocument(loginLogDlq);

        // then
        assertThat(doc1.topic()).isEqualTo("account-history");
        assertThat(doc2.topic()).isEqualTo("account-login");
    }

    @Test
    @DisplayName("[toDocument] 복잡한 JSON payload도 변환 가능하다")
    void toDocument_complexPayload() {
        // given
        String complexPayload = """
            {
                "accountId": 1,
                "type": "CREATE",
                "detailInfo": "계정 생성",
                "metadata": {
                    "ip": "127.0.0.1",
                    "userAgent": "Mozilla/5.0"
                }
            }
            """;

        DlqLog dlqLog = DlqLog.builder()
            .topic("account-history")
            .payload(complexPayload)
            .regDateTime("2025-10-25 10:00:00")
            .build();

        // when
        DlqLogDocument document = DlqLogMapper.toDocument(dlqLog);

        // then
        assertThat(document.payload()).isEqualTo(complexPayload);
        assertThat(document.payload()).contains("metadata");
        assertThat(document.payload()).contains("ip");
    }

    @Test
    @DisplayName("[toDocument] 잘못된 형식의 payload도 문자열로 저장 가능하다")
    void toDocument_invalidPayload() {
        // given
        DlqLog dlqLog = DlqLog.builder()
            .topic("account-history")
            .payload("invalid json string {")
            .regDateTime("2025-10-25 10:00:00")
            .build();

        // when
        DlqLogDocument document = DlqLogMapper.toDocument(dlqLog);

        // then
        assertThat(document.payload()).isEqualTo("invalid json string {");
    }
}