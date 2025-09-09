package com.order.applicatoin.service.cancel_order;

import static com.order.infrastructure.util.JsonUtil.toJsonString;

import com.order.applicatoin.port.in.CancelOrderUseCase;
import com.order.applicatoin.port.in.command.CancelOrderCommand;
import com.order.applicatoin.port.out.MessageProducerPort;
import com.order.applicatoin.port.out.OrderStoragePort;
import com.order.domain.model.Order;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CancelOrderService implements CancelOrderUseCase {

    @Value("${kafka.topic.cancel-order}")
    private String topicName;
    private final OrderStoragePort orderStoragePort;
    private final MessageProducerPort messageProducerPort;

    @Override
    @Transactional
    public CancelOrderServiceResponse cancel(CancelOrderCommand command) {
        // TODO: 구매한 사용자만 취소할 수 있도록 / 이미 취소된 상품인 경우 예외 처리
        Order order = orderStoragePort.findById(command.orderId());
        order.cancel(LocalDateTime.now());
        orderStoragePort.cancel(order);
        messageProducerPort.sendMessage(topicName, toJsonString(order.products()));
        return CancelOrderServiceResponse.ofSuccess();
    }
}
