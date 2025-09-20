package com.order.adapter.in.controller.find_customer_orders;

import com.order.applicatoin.port.in.command.FindCustomerOrdersCommand;
import com.order.domain.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindCustomerOrdersRequestTest {

    @Nested
    @DisplayName("[toCommand] API 요청 객체를 커멘드 객체로 변경하는 메소드")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] 페이지를 입력하지 않았을 때 페이지를 0 으로 초기화하여 command 를 생성한다")
        void success() {
            // given
            FindCustomerOrdersRequest request =
                FindCustomerOrdersRequest.builder()
                    .size(10)
                    .build();
            Account account = Account.builder()
                .id(12L)
                .build();

            // when
            FindCustomerOrdersCommand command = request.toCommand(account);

            // then
            assert command.customerId().equals(account.id());
            assert command.size().equals(request.size());
            assert command.page() == 0;
        }

        @Test
        @DisplayName("[success] 사이즈를 입력하지 않았을 때 사이즈가 10 으로 초기화하여 command 를 생성한다")
        void success2() {
            // given
            FindCustomerOrdersRequest request =
                FindCustomerOrdersRequest.builder()
                    .page(10)
                    .build();
            Account account = Account.builder()
                .id(12L)
                .build();

            // when
            FindCustomerOrdersCommand command = request.toCommand(account);

            // then
            assert command.customerId().equals(account.id());
            assert command.size() == 10;
            assert command.page().equals(request.page());
        }
    }
}