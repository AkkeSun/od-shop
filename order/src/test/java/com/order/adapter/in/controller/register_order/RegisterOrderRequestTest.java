package com.order.adapter.in.controller.register_order;

import com.order.adapter.in.controller.register_order.RegisterOrderRequest.RegisterOrderRequestItem;
import com.order.applicatoin.port.in.command.RegisterOrderCommand;
import com.order.applicatoin.port.in.command.RegisterOrderCommand.RegisterOrderCommandItem;
import com.order.domain.model.Account;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterOrderRequestTest {

    @Nested
    @DisplayName("[toCommand] API 요청 객체를 커멘드 객체로 변경하는 메소드")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] 페이지를 입력하지 않았을 때 페이지를 0 으로 초기화하여 command 를 생성한다")
        void success() {
            // given
            RegisterOrderRequestItem item = RegisterOrderRequestItem.builder()
                .productId(10L)
                .reserveId(20L)
                .build();
            Account account = Account.builder()
                .id(20L)
                .build();
            RegisterOrderRequest request = RegisterOrderRequest.builder()
                .reserveInfos(List.of(item))
                .totalPrice(50)
                .receiverName("receiverName")
                .receiverTel("receiverTel")
                .receiverAddress("receiverAddress")
                .build();

            // when
            RegisterOrderCommand result = request.toCommand(account);

            // then
            assert result.accountId().equals(account.id());
            assert result.totalPrice() == request.totalPrice();
            assert result.receiverName().equals(request.receiverName());
            assert result.receiverTel().equals(request.receiverTel());
            assert result.receiverAddress().equals(request.receiverAddress());
            assert result.reserveInfos().size() == 1;
            RegisterOrderCommandItem first = result.reserveInfos().getFirst();
            assert first.productId().equals(item.productId());
            assert first.reserveId().equals(item.reserveId());
        }
    }
}