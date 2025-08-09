package com.product.adapter.in.grpc;

import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.product.application.port.in.ReserveProductUseCase;
import com.product.application.port.in.command.ReserveProductCommand;
import com.product.application.service.reserve_product.ReserveProductServiceResponse;
import com.product.infrastructure.util.GrpcUtil;
import grpc.product.CreateProductReservationRequest;
import grpc.product.CreateProductReservationResponse;
import grpc.product.CreateProductReservationServiceGrpc.CreateProductReservationServiceImplBase;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
class CreateReservationStub extends CreateProductReservationServiceImplBase {

    private final ReserveProductUseCase useCase;


    @Override
    public void createReserve(CreateProductReservationRequest request,
        StreamObserver<CreateProductReservationResponse> responseObserver) {
        try {

            log.info("[gRPC] createReservation request - {}", toJsonString(request));
            ReserveProductServiceResponse response = useCase.reserve(ReserveProductCommand.builder()
                .productId(request.getProductId())
                .productQuantity(request.getQuantity())
                .customerId(request.getCustomerId())
                .build());

            responseObserver.onNext(CreateProductReservationResponse.newBuilder()
                .setReserveId(response.reserveId())
                .build());

            responseObserver.onCompleted();
            log.info("[gRPC] createReservation response - {}", toJsonString(response));

        } catch (Exception e) {
            Status status = GrpcUtil.getStatus(e);
            log.info("[gRPC] createReservation error - {}", toJsonString(status));

            responseObserver.onError(status
                .withDescription(status.getDescription())
                .asRuntimeException());
        }
    }
}
