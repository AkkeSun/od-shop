package com.order.application.service.register_order;

import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.order.application.port.in.RegisterOrderUseCase;
import com.order.application.port.in.command.RegisterOrderCommand;
import com.order.application.port.in.command.RegisterOrderCommand.RegisterOrderCommandItem;
import com.order.application.port.out.MessageProducerPort;
import com.order.application.port.out.OrderStoragePort;
import com.order.application.port.out.ProductClientPort;
import com.order.application.port.out.RedisStoragePort;
import com.order.domain.model.Order;
import com.order.domain.model.OrderProduct;
import com.order.infrastructure.exception.CustomGrpcResponseError;
import io.grpc.StatusRuntimeException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RegisterOrderService implements RegisterOrderUseCase {

    @Value("${kafka.topic.rollback-reserve}")
    private String rollbackTopic;

    private final RedisStoragePort redisStoragePort;

    private final ProductClientPort clientPort;

    private final OrderStoragePort orderStoragePort;

    private final MessageProducerPort messageProducerPort;

    @Override
    public RegisterOrderServiceResponse register(RegisterOrderCommand command) {
        List<OrderProduct> confirmReserves = new ArrayList<>();

        for (RegisterOrderCommandItem item : command.reserveInfos()) {
            try {
                confirmReserves.add(clientPort.confirmReserve(item));

            } catch (StatusRuntimeException e) {
                for (OrderProduct confirmItem : confirmReserves) {
                    confirmItem.updateCustomerId(confirmItem.getCustomerId());
                    messageProducerPort.sendMessage(rollbackTopic, toJsonString(confirmItem));
                }

                throw new CustomGrpcResponseError(
                    e.getStatus().getDescription() + " - " + item.productId());
            }
        }

        Order savedOrder = orderStoragePort.register(Order.of(command, confirmReserves));
        redisStoragePort.delete(redisStoragePort.getKeys(
            String.format("customer-order::%s*", savedOrder.customerId())));

        return RegisterOrderServiceResponse.of(savedOrder);
    }
}
