package com.order.applicatoin.service.reserve_product;

import static com.order.infrastructure.util.JsonUtil.toJsonString;

import com.order.applicatoin.port.in.ReserveProductUseCase;
import com.order.applicatoin.port.in.command.ReserveProductCommand;
import com.order.applicatoin.port.in.command.ReserveProductCommand.ReserveProductCommandItem;
import com.order.applicatoin.port.out.MessageProducerPort;
import com.order.applicatoin.port.out.ProductClientPort;
import com.order.applicatoin.port.out.RedisStoragePort;
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

    @Value("${spring.data.redis.key.customer-order}")
    private String redisKey;

    private final RedisStoragePort redisStoragePort;

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

        redisStoragePort.delete(redisStoragePort.getKeys("customer-order::" + accountId + "*"));
        return responseList;
    }
}
