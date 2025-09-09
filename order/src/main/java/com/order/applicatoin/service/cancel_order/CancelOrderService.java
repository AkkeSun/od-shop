package com.order.applicatoin.service.cancel_order;

import static com.order.infrastructure.exception.ErrorCode.Business_ALREADY_CANCEL_ORDCER;
import static com.order.infrastructure.exception.ErrorCode.Business_NO_CUSTOMER;
import static com.order.infrastructure.util.JsonUtil.toJsonString;

import com.order.applicatoin.port.in.CancelOrderUseCase;
import com.order.applicatoin.port.in.command.CancelOrderCommand;
import com.order.applicatoin.port.out.MessageProducerPort;
import com.order.applicatoin.port.out.OrderStoragePort;
import com.order.domain.model.Order;
import com.order.infrastructure.exception.CustomBusinessException;
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
