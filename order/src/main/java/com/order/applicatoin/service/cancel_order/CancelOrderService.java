package com.order.applicatoin.service.cancel_order;

import com.order.applicatoin.port.in.CancelOrderUseCase;
import com.order.applicatoin.port.in.command.CancelOrderCommand;
import com.order.applicatoin.port.out.OrderStoragePort;
import com.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CancelOrderService implements CancelOrderUseCase {

    private final OrderStoragePort orderStoragePort;

    @Override
    public CancelOrderServiceResponse cancel(CancelOrderCommand command) {
        Order order = orderStoragePort.findById(command.orderId());

        // 상태값 변경 + 히스토리 등록

        // 메시지 발송
        return null;
    }
}
