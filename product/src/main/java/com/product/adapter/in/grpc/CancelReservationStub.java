package com.product.adapter.in.grpc;

import static com.common.infrastructure.util.GrpcUtil.getGrpcRequestJson;
import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.common.infrastructure.util.GrpcUtil;
import com.product.application.port.in.CancelReservationUseCase;
import com.product.application.port.in.command.CancelReservationCommand;
import com.product.application.service.cancel_reservation.CancelReservationServiceResponse;
import grpc.product.CancelProductReservationRequest;
import grpc.product.CancelProductReservationResponse;
import grpc.product.CancelProductReservationServiceGrpc.CancelProductReservationServiceImplBase;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class CancelReservationStub extends CancelProductReservationServiceImplBase {

    private final CancelReservationUseCase useCase;

    @Override
    public void cancelReservation(CancelProductReservationRequest request,
        StreamObserver<CancelProductReservationResponse> responseObserver) {
        try {

            log.info("[gRPC] cancelReservation request - {}", getGrpcRequestJson(request));

            CancelReservationServiceResponse response = useCase.cancel(CancelReservationCommand.builder()
                    .productId(request.getProductId())
                    .reserveId(request.getReservationId())
                    .build());

            responseObserver.onNext(CancelProductReservationResponse.newBuilder()
                .setResult(response.result())
                .build());
            responseObserver.onCompleted();
            log.info("[gRPC] cancelReservation response - {}", toJsonString(response));

        } catch (Exception e) {
            Status status = GrpcUtil.getStatus(e);
            log.info("[gRPC] cancelReservation error - {}", toJsonString(status));

            responseObserver.onError(status
                .withDescription(status.getDescription())
                .asRuntimeException());
        }
    }
}
