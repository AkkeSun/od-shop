package com.order.application.service.cancel_order;

import static com.common.infrastructure.exception.ErrorCode.Business_ALREADY_CANCEL_ORDCER;
import static com.common.infrastructure.exception.ErrorCode.Business_NO_CUSTOMER;
import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.common.infrastructure.exception.CustomBusinessException;
import com.order.application.port.in.CancelOrderUseCase;
import com.order.application.port.in.command.CancelOrderCommand;
import com.order.application.port.out.MessageProducerPort;
import com.order.application.port.out.OrderStoragePort;
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
        Order order = orderStoragePort.findById(command.orderId());
        if (!order.isCustomer(command.account())) {
            throw new CustomBusinessException(Business_NO_CUSTOMER);
        }
        if (order.isCanceled()) {
            throw new CustomBusinessException(Business_ALREADY_CANCEL_ORDCER);
        }

        order.cancel(LocalDateTime.now());
        orderStoragePort.cancel(order);
        messageProducerPort.sendMessage(topicName, toJsonString(order.products()));
        return CancelOrderServiceResponse.ofSuccess();
    }
}
