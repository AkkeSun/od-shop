package com.product.adapter.in.grpc;

import static com.common.infrastructure.util.GrpcUtil.getGrpcRequestJson;
import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.common.infrastructure.util.GrpcUtil;
import com.product.application.port.in.CreateReservationUseCase;
import com.product.application.port.in.command.CreateReservationCommand;
import com.product.application.service.create_reservation.CreateReservationServiceResponse;
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

    private final CreateReservationUseCase useCase;


    @Override
    public void createReserve(CreateProductReservationRequest request,
        StreamObserver<CreateProductReservationResponse> responseObserver) {
        try {

            log.info("[gRPC] createReservation request - {}", getGrpcRequestJson(request));
            CreateReservationServiceResponse response = useCase.reserve(
                CreateReservationCommand.builder()
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
