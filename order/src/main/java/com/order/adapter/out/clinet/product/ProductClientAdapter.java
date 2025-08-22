package com.order.adapter.out.clinet.product;

import com.order.applicatoin.port.in.command.RegisterOrderCommand.RegisterOrderCommandItem;
import com.order.applicatoin.port.in.command.ReserveProductCommand.ReserveProductCommandItem;
import com.order.applicatoin.port.out.ProductClientPort;
import grpc.product.ConfirmProductReservationRequest;
import grpc.product.ConfirmProductReservationServiceGrpc.ConfirmProductReservationServiceBlockingStub;
import grpc.product.CreateProductReservationRequest;
import grpc.product.CreateProductReservationResponse;
import grpc.product.CreateProductReservationServiceGrpc.CreateProductReservationServiceBlockingStub;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
class ProductClientAdapter implements ProductClientPort {

    @GrpcClient("product")
    private CreateProductReservationServiceBlockingStub reservationService;

    @GrpcClient("product")
    private ConfirmProductReservationServiceBlockingStub confirmReservationService;


    @Override
    public Long reserveProduct(ReserveProductCommandItem command, Long accountId) {
        CreateProductReservationResponse response = reservationService.createReserve(
            CreateProductReservationRequest.newBuilder()
                .setProductId(command.productId())
                .setCustomerId(accountId)
                .setQuantity(command.quantity())
                .build());

        return response.getReserveId();
    }

    @Override
    public void confirmReserve(RegisterOrderCommandItem command) {
        confirmReservationService.confirmReservation(ConfirmProductReservationRequest.newBuilder()
            .setReservationId(command.reserveId())
            .setProductId(command.productId())
            .build());
    }
}
