package com.order.application.service.reserve_product;

import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.common.infrastructure.exception.CustomGrpcResponseError;
import com.order.application.port.in.ReserveProductUseCase;
import com.order.application.port.in.command.ReserveProductCommand;
import com.order.application.port.in.command.ReserveProductCommand.ReserveProductCommandItem;
import com.order.application.port.out.MessageProducerPort;
import com.order.application.port.out.ProductClientPort;
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
    public List<ReserveProductServiceResponse> reservation(ReserveProductCommand command) {
        List<ReserveProductServiceResponse> responseList = new ArrayList<>();

        Long accountId = command.accountId();
        for (ReserveProductCommandItem item : command.items()) {
            try {
                responseList.add(ReserveProductServiceResponse.builder()
                    .productId(item.productId())
                    .reserveId(productClientPort.reserveProduct(item, accountId))
                    .build());

            } catch (StatusRuntimeException e) {
                for (ReserveProductServiceResponse response : responseList) {
                    messageProducerPort.sendMessage(cancelTopic, toJsonString(response));
                }

                throw new CustomGrpcResponseError(
                    e.getStatus().getDescription() + " - " + item.productId());
            }
        }
        return responseList;
    }
}
