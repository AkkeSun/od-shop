package com.product.adapter.in.controller.register_review;

import com.product.application.port.in.command.RegisterReviewCommand;
import com.product.domain.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterReviewRequestTest {

    @Nested
    @DisplayName("[toCommand] apiRequest 를 Command로 변환하는 메소드")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] apiRequest 를 Command로 잘 변환되는지 확인한다.")
        void success() {
            // given
            Long productId = 1L;
            Account account = Account.builder()
                .id(2L)
                .email("email")
                .build();
            RegisterReviewRequest request = RegisterReviewRequest.builder()
                .review("좋아요")
                .score(4.5)
                .build();

            // when
            RegisterReviewCommand command = request.toCommand(productId, account);

            // then
            assert command.productId().equals(productId);
            assert command.account().id().equals(account.id());
            assert command.account().email().equals(account.email());
            assert command.score() == request.score();
            assert command.review().equals(request.review());
        }
    }
}