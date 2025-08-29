package com.order.applicatoin.service.register_order;

import static com.order.infrastructure.util.JsonUtil.toJsonString;

import com.order.applicatoin.port.in.RegisterOrderUseCase;
import com.order.applicatoin.port.in.command.RegisterOrderCommand;
import com.order.applicatoin.port.in.command.RegisterOrderCommand.RegisterOrderCommandItem;
import com.order.applicatoin.port.out.MessageProducerPort;
import com.order.applicatoin.port.out.OrderStoragePort;
import com.order.applicatoin.port.out.ProductClientPort;
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
        return RegisterOrderServiceResponse.of(savedOrder);
    }
}
