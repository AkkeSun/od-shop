package com.account.domain.model;

import com.account.applicaiton.port.in.command.UpdateAccountCommand;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AccountTest {

    @Nested
    @DisplayName("[update] 사용자 정보를 수정하는 메소드")
    class Describe_update {

        @Test
        @DisplayName("[success] 수정한 내역이 있다면 수정 목록을 응답한다.")
        void success1() {
            // given
            Account account = Account.builder()
                .password("1234")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();

            UpdateAccountCommand command = UpdateAccountCommand.builder()
                .username("od2")
                .build();

            // when
            List<String> updateList = account.update(command);

            // then
            assert updateList.getFirst().equals("username");
            assert account.getUsername().equals("od2");
        }

        @Test
        @DisplayName("[success] 수정한 내역이 없다면 빈 목록을 응답한다.")
        void success2() {
            // given
            Account account = Account.builder()
                .password("1234")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            UpdateAccountCommand command = UpdateAccountCommand.builder()
                .username("od")
                .build();

            // when
            List<String> updateList = account.update(command);

            // then
            assert updateList.isEmpty();
        }
    }
}