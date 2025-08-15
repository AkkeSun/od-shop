package com.order.adapter.out.clinet.product;

import com.order.applicatoin.port.in.command.ReserveProductCommand.ReserveProductCommandItem;
import com.order.applicatoin.port.out.ProductClientPort;
import grpc.product.CreateProductReservationRequest;
import grpc.product.CreateProductReservationResponse;
import grpc.product.CreateProductReservationServiceGrpc.CreateProductReservationServiceBlockingStub;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class ProductClientAdapter implements ProductClientPort {

    @GrpcClient("product")
    private CreateProductReservationServiceBlockingStub reservationService;

    @Override
    public Long reserveProduct(ReserveProductCommandItem command, Long accountId) {
        try {
            CreateProductReservationResponse response = reservationService.createReserve(
                CreateProductReservationRequest.newBuilder()
                    .setProductId(command.productId())
                    .setCustomerId(accountId)
                    .setQuantity(command.quantity())
                    .build());

            return response.getReserveId();
        } catch (StatusRuntimeException e) {
            log.error("[gRPC] reserveProduct - accountId: {}, productId: {}, message: {}",
                accountId, command.productId(), e.getStatus().getDescription());

            throw new RuntimeException(e);
        }
    }
}
