package com.product.adapter.in.controller.find_recommend_product;

import com.product.application.port.in.command.FindRecommendProductCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindRecommendProductRequestTest {


    @Nested
    @DisplayName("[toCommand] API 요청 정보를 command 로 변환하는 메소드")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] API 요청 정보를 command 로 잘 변환하는지 확인한다.")
        void success() {
            // given
            FindRecommendProductRequest request = FindRecommendProductRequest.builder()
                .searchDate("20250501")
                .build();
            Account account = Account.builder().id(10L).build();

            // when
            FindRecommendProductCommand command = request.toCommand(account);

            // then
            assert command.searchDate().equals(request.getSearchDate());
            assert command.accountId().equals(account.id());
        }

    }

}