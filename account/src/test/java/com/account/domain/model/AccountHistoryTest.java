package com.account.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AccountHistoryTest {

    @Nested
    @DisplayName("[createAccountHistoryForDelete] 삭제정보를 위한 히스토리 생성 메소드")
    class Describe_createAccountHistoryForDelete {

        @Test
        @DisplayName("[success] 삭제정보를 위한 히스토리를 생성한다.")
        void success() {
            // given
            Long accountId = 1L;

            // when
            AccountHistory accountHistory = AccountHistory.createAccountHistoryForDelete(accountId);

            // then
            assert accountHistory.accountId().equals(accountId);
            assert accountHistory.type().equals("delete");
        }
    }

    @Nested
    @DisplayName("[createAccountHistoryForUpdate] 수정정보를 위한 히스토리 생성 메소드")
    class Describe_createAccountHistoryForUpdate {

        @Test
        @DisplayName("[success] 수정정보를 위한 히스토리를 생성한다.")
        void success() {
            // given
            Long accountId = 1L;
            String updateList = "username, password, userTel, address";

            // when
            AccountHistory accountHistory = AccountHistory.createAccountHistoryForUpdate(accountId,
                updateList);

            // then
            assert accountHistory.accountId().equals(accountId);
            assert accountHistory.type().equals("update");
            assert accountHistory.detailInfo().equals(updateList);
        }
    }
}