package com.product.adapter.in.controller.find_comment_list;

import com.product.application.port.in.command.FindCommentListCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindCommentListRequestTest {

    @Nested
    @DisplayName("[toCommand] api 요청을 Command로 변환한다.")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] 요청을 Command로 변환한다.")
        void success() {
            // given
            FindCommentListRequest request = FindCommentListRequest.builder()
                .page(1)
                .size(10)
                .build();
            Long productId = 1L;

            // when
            FindCommentListCommand command = request.toCommand(productId);

            // then
            assert command.productId().equals(productId);
            assert command.page() == 1;
            assert command.size() == 10;
        }
    }
}