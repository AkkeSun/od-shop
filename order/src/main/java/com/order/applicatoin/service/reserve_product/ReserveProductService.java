package com.order.applicatoin.service.reserve_product;

import com.order.applicatoin.port.in.ReserveProductUseCase;
import com.order.applicatoin.port.in.command.ReserveProductCommand;
import com.order.applicatoin.port.in.command.ReserveProductCommand.ReserveProductCommandItem;
import com.order.applicatoin.port.out.MessageProducerPort;
import com.order.applicatoin.port.out.ProductClientPort;
import com.order.infrastructure.exception.CustomGrpcResponseError;
import io.grpc.StatusRuntimeException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ReserveProductService implements ReserveProductUseCase {

    @Value("${kafka.topic.cancel-reserve}")
    private String cancelTopic;

    private final ProductClientPort productClientPort;

    private final MessageProducerPort messageProducerPort;

    @Override
    public ReserveProductServiceResponse reservation(ReserveProductCommand command) {
        List<Long> reservationIds = new ArrayList<>();

        Long accountId = command.accountId();
        for (ReserveProductCommandItem item : command.items()) {
            try {
                reservationIds.add(productClientPort.reserveProduct(item, accountId));

            } catch (StatusRuntimeException e) {
                for (Long reservationId : reservationIds) {
                    messageProducerPort.sendMessage(cancelTopic, String.valueOf(reservationId));
                }

                throw new CustomGrpcResponseError(
                    e.getStatus().getDescription() + " - " + item.productId());
            }
        }
        return ReserveProductServiceResponse.of(reservationIds);
    }
}
