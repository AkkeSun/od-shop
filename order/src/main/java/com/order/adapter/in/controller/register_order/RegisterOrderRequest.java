package com.order.adapter.in.controller.register_order;

import com.common.infrastructure.resolver.LoginAccountInfo;
import com.order.application.port.in.command.RegisterOrderCommand;
import com.order.application.port.in.command.RegisterOrderCommand.RegisterOrderCommandItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
record RegisterOrderRequest(

    @NotEmpty(message = "예약 정보는 필수값 입니다")
    List<RegisterOrderRequestItem> reserveInfos,

    @NotNull(message = "총 금액은 필수값 입니다")
    Integer totalPrice,

    @NotBlank(message = "받는사람 이름은 필수값 입니다")
    String receiverName,

    @NotBlank(message = "받는사람 전화번호는 필수값 입니다")
    String receiverTel,

    @NotBlank(message = "받는사람 주소는 필수값 입니다")
    String receiverAddress

) {

    RegisterOrderCommand toCommand(LoginAccountInfo loginInfo) {
        return RegisterOrderCommand.builder()
            .accountId(loginInfo.getId())
            .reserveInfos(reserveInfos.stream()
                .map(item -> RegisterOrderCommandItem.builder()
                    .productId(item.productId)
                    .reserveId(item.reserveId)
                    .build())
                .toList())
            .totalPrice(totalPrice)
            .receiverName(receiverName)
            .receiverTel(receiverTel)
            .receiverAddress(receiverAddress)
            .build();
    }

    @Builder
    record RegisterOrderRequestItem(
        Long productId,
        Long reserveId
    ) {

    }
}
