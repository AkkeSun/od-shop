package com.order.applicatoin.service.cancel_order;

import com.order.applicatoin.port.in.CancelOrderUseCase;
import com.order.applicatoin.port.in.command.CancelOrderCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CancelOrderService implements CancelOrderUseCase {

    @Override
    public CancelOrderServiceResponse cancel(CancelOrderCommand command) {

        // 주문조회

        // 상태값 변경 + 히스토리 등록

        // 메시지 발송
        return null;
    }
}
