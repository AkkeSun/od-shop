package com.product.adapter.in.controller.find_review_list;

import com.product.application.port.in.command.FindReviewListCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindReviewListRequestTest {

    @Nested
    @DisplayName("[toCommand] api 요청을 Command로 변환한다.")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] 요청을 Command로 변환한다.")
        void success() {
            // given
            FindReviewListRequest request = FindReviewListRequest.builder()
                .page(1)
                .size(10)
                .build();
            Long productId = 1L;

            // when
            FindReviewListCommand command = request.toCommand(productId);

            // then
            assert command.productId().equals(productId);
            assert command.pageable().getPageNumber() == 1;
            assert command.pageable().getPageSize() == 10;
        }
    }
}