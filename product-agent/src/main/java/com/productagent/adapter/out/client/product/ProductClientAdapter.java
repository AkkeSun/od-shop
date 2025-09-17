package com.productagent.adapter.out.client.product;

import com.productagent.application.port.out.ProductClientPort;
import com.productagent.domain.model.RefundProductMessage;
import grpc.product.UpdateProductQuantityServiceGrpc.UpdateProductQuantityServiceBlockingStub;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class ProductClientAdapter implements ProductClientPort {

    @GrpcClient("product")
    private UpdateProductQuantityServiceBlockingStub product;

    @Override
    public void updateProductQuantity(RefundProductMessage message) {
        try {
            product.updateProductQuantity(message.toClientMessage());
        } catch (StatusRuntimeException e) {
            log.error("[gRPC] updateProductQuantity - productId: {}, message: {}",
                message.productId(), e.getStatus().getDescription());

            throw new RuntimeException(e);
        }
    }
}
