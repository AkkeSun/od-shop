package com.accountagent.adapter.out.persistence.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import com.accountagent.domain.model.AccountHistory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("AccountHistoryMapper 테스트")
class AccountHistoryMapperTest {

    @Test
    @DisplayName("[toDocument] AccountHistory를 AccountHistoryDocument로 변환한다")
    void toDocument() {
        // given
        AccountHistory history = AccountHistory.builder()
            .accountId(1L)
            .type("CREATE")
            .detailInfo("계정 생성")
            .regDateTime("2025-10-25 10:00:00")
            .build();

        // when
        AccountHistoryDocument document = AccountHistoryMapper.toDocument(history);

        // then
        assertThat(document).isNotNull();
        assertThat(document.accountId()).isEqualTo(1L);
        assertThat(document.type()).isEqualTo("CREATE");
        assertThat(document.detailInfo()).isEqualTo("계정 생성");
        assertThat(document.regDateTime()).isEqualTo("2025-10-25 10:00:00");
    }

    @Test
    @DisplayName("[toDocument] 모든 필드가 null인 AccountHistory도 변환 가능하다")
    void toDocument_nullFields() {
        // given
        AccountHistory history = AccountHistory.builder()
            .accountId(null)
            .type(null)
            .detailInfo(null)
            .regDateTime(null)
            .build();

        // when
        AccountHistoryDocument document = AccountHistoryMapper.toDocument(history);

        // then
        assertThat(document).isNotNull();
        assertThat(document.accountId()).isNull();
        assertThat(document.type()).isNull();
        assertThat(document.detailInfo()).isNull();
        assertThat(document.regDateTime()).isNull();
    }

    @Test
    @DisplayName("[toDocument] UPDATE 타입의 AccountHistory를 변환한다")
    void toDocument_updateType() {
        // given
        AccountHistory history = AccountHistory.builder()
            .accountId(2L)
            .type("UPDATE")
            .detailInfo("계정 정보 수정")
            .regDateTime("2025-10-25 11:00:00")
            .build();

        // when
        AccountHistoryDocument document = AccountHistoryMapper.toDocument(history);

        // then
        assertThat(document.accountId()).isEqualTo(2L);
        assertThat(document.type()).isEqualTo("UPDATE");
        assertThat(document.detailInfo()).isEqualTo("계정 정보 수정");
    }

    @Test
    @DisplayName("[toDocument] DELETE 타입의 AccountHistory를 변환한다")
    void toDocument_deleteType() {
        // given
        AccountHistory history = AccountHistory.builder()
            .accountId(3L)
            .type("DELETE")
            .detailInfo("계정 삭제")
            .regDateTime("2025-10-25 12:00:00")
            .build();

        // when
        AccountHistoryDocument document = AccountHistoryMapper.toDocument(history);

        // then
        assertThat(document.accountId()).isEqualTo(3L);
        assertThat(document.type()).isEqualTo("DELETE");
        assertThat(document.detailInfo()).isEqualTo("계정 삭제");
    }
}