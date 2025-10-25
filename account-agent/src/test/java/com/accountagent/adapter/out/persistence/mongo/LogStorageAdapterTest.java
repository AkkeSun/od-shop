package com.accountagent.adapter.out.persistence.mongo;

import static org.assertj.core.api.Assertions.assertThat;

import com.accountagent.domain.model.AccountHistory;
import com.accountagent.domain.model.DlqLog;
import com.accountagent.domain.model.LoginLog;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

@DataMongoTest
@Import(LogStorageAdapter.class)
class LogStorageAdapterTest {

    @Autowired
    LogStorageAdapter adapter;

    @Autowired
    MongoTemplate mongoTemplate;

    @AfterEach
    void tearDown() {
        mongoTemplate.getCollectionNames().forEach(mongoTemplate::dropCollection);
    }

    @Nested
    @DisplayName("[registerHistoryLog] AccountHistory를 저장하는 메소드")
    class Describe_registerHistoryLog {

        @Test
        @DisplayName("[success] AccountHistory를 MongoDB에 저장한다")
        void success() {
            // given
            AccountHistory history = AccountHistory.builder()
                .accountId(1L)
                .type("CREATE")
                .detailInfo("계정 생성")
                .regDateTime("2025-10-25 10:00:00")
                .build();

            // when
            adapter.registerHistoryLog(history);

            // then
            String expectedCollectionName = "history_" + LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            assertThat(mongoTemplate.collectionExists(expectedCollectionName)).isTrue();

            AccountHistoryDocument savedDocument = mongoTemplate.findAll(
                AccountHistoryDocument.class, expectedCollectionName).get(0);

            assertThat(savedDocument.accountId()).isEqualTo(1L);
            assertThat(savedDocument.type()).isEqualTo("CREATE");
            assertThat(savedDocument.detailInfo()).isEqualTo("계정 생성");
            assertThat(savedDocument.regDateTime()).isEqualTo("2025-10-25 10:00:00");
        }

        @Test
        @DisplayName("[success] 여러 개의 AccountHistory를 저장한다")
        void success_multiple() {
            // given
            AccountHistory history1 = AccountHistory.builder()
                .accountId(1L)
                .type("CREATE")
                .detailInfo("계정 생성")
                .regDateTime("2025-10-25 10:00:00")
                .build();

            AccountHistory history2 = AccountHistory.builder()
                .accountId(2L)
                .type("UPDATE")
                .detailInfo("계정 수정")
                .regDateTime("2025-10-25 11:00:00")
                .build();

            // when
            adapter.registerHistoryLog(history1);
            adapter.registerHistoryLog(history2);

            // then
            String expectedCollectionName = "history_" + LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            assertThat(mongoTemplate.findAll(AccountHistoryDocument.class, expectedCollectionName))
                .hasSize(2);
        }
    }

    @Nested
    @DisplayName("[registerLoginLog] LoginLog를 저장하는 메소드")
    class Describe_registerLoginLog {

        @Test
        @DisplayName("[success] LoginLog를 MongoDB에 저장한다")
        void success() {
            // given
            LoginLog loginLog = LoginLog.builder()
                .accountId(1L)
                .email("test@example.com")
                .loginDateTime("2025-10-25 10:00:00")
                .build();

            // when
            adapter.registerLoginLog(loginLog);

            // then
            String expectedCollectionName = "login_" + LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            assertThat(mongoTemplate.collectionExists(expectedCollectionName)).isTrue();

            LoginLogDocument savedDocument = mongoTemplate.findAll(
                LoginLogDocument.class, expectedCollectionName).get(0);

            assertThat(savedDocument.accountId()).isEqualTo(1L);
            assertThat(savedDocument.email()).isEqualTo("test@example.com");
            assertThat(savedDocument.loginDateTime()).isEqualTo("2025-10-25 10:00:00");
        }

        @Test
        @DisplayName("[success] 여러 개의 LoginLog를 저장한다")
        void success_multiple() {
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
            adapter.registerLoginLog(loginLog1);
            adapter.registerLoginLog(loginLog2);

            // then
            String expectedCollectionName = "login_" + LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            assertThat(mongoTemplate.findAll(LoginLogDocument.class, expectedCollectionName))
                .hasSize(2);
        }
    }

    @Nested
    @DisplayName("[registerDlqLog] DlqLog를 저장하는 메소드")
    class Describe_registerDlqLog {

        @Test
        @DisplayName("[success] DlqLog를 MongoDB에 저장한다")
        void success() {
            // given
            DlqLog dlqLog = DlqLog.builder()
                .topic("test-topic")
                .payload("test payload")
                .regDateTime("2025-10-25 10:00:00")
                .build();

            // when
            adapter.registerDlqLog(dlqLog);

            // then
            String expectedCollectionName = "dlq_" + LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

            assertThat(mongoTemplate.collectionExists(expectedCollectionName)).isTrue();

            DlqLogDocument savedDocument = mongoTemplate.findAll(
                DlqLogDocument.class, expectedCollectionName).get(0);

            assertThat(savedDocument.topic()).isEqualTo("test-topic");
            assertThat(savedDocument.payload()).isEqualTo("test payload");
            assertThat(savedDocument.regDateTime()).isEqualTo("2025-10-25 10:00:00");
        }
    }

    @Nested
    @DisplayName("[deleteLoginLog] LoginLog 컬렉션을 삭제하는 메소드")
    class Describe_deleteLoginLog {

        @Test
        @DisplayName("[success] 특정 날짜의 LoginLog 컬렉션을 삭제한다")
        void success() {
            // given
            String regDate = "20251025";
            String collectionName = "login_" + regDate;

            // 컬렉션 생성
            LoginLogDocument doc = LoginLogDocument.builder()
                .accountId(1L)
                .email("test@example.com")
                .loginDateTime("2025-10-25 10:00:00")
                .build();
            mongoTemplate.save(doc, collectionName);

            assertThat(mongoTemplate.collectionExists(collectionName)).isTrue();

            // when
            adapter.deleteLoginLog(regDate);

            // then
            assertThat(mongoTemplate.collectionExists(collectionName)).isFalse();
        }

        @Test
        @DisplayName("[success] 존재하지 않는 컬렉션을 삭제해도 예외가 발생하지 않는다")
        void success_collectionNotExists() {
            // given
            String regDate = "20251024";
            String collectionName = "login_" + regDate;

            assertThat(mongoTemplate.collectionExists(collectionName)).isFalse();

            // when & then
            adapter.deleteLoginLog(regDate); // 예외 발생하지 않음
        }
    }

    @Nested
    @DisplayName("[deleteHistoryLog] AccountHistory 컬렉션을 삭제하는 메소드")
    class Describe_deleteHistoryLog {

        @Test
        @DisplayName("[success] 특정 날짜의 AccountHistory 컬렉션을 삭제한다")
        void success() {
            // given
            String regDate = "20251025";
            String collectionName = "history_" + regDate;

            // 컬렉션 생성
            AccountHistoryDocument doc = AccountHistoryDocument.builder()
                .accountId(1L)
                .type("CREATE")
                .detailInfo("계정 생성")
                .regDateTime("2025-10-25 10:00:00")
                .build();
            mongoTemplate.save(doc, collectionName);

            assertThat(mongoTemplate.collectionExists(collectionName)).isTrue();

            // when
            adapter.deleteHistoryLog(regDate);

            // then
            assertThat(mongoTemplate.collectionExists(collectionName)).isFalse();
        }

        @Test
        @DisplayName("[success] 존재하지 않는 컬렉션을 삭제해도 예외가 발생하지 않는다")
        void success_collectionNotExists() {
            // given
            String regDate = "20251024";
            String collectionName = "history_" + regDate;

            assertThat(mongoTemplate.collectionExists(collectionName)).isFalse();

            // when & then
            adapter.deleteHistoryLog(regDate); // 예외 발생하지 않음
        }
    }
}
