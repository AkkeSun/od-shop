package com.product.adapter.in.grpc;

import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.product.application.port.in.ConfirmReservationUseCase;
import com.product.application.port.in.command.ConfirmReservationCommand;
import com.product.application.service.confirm_reeservation.ConfirmReservationServiceResponse;
import com.product.infrastructure.util.GrpcUtil;
import grpc.product.ConfirmProductReservationRequest;
import grpc.product.ConfirmProductReservationResponse;
import grpc.product.ConfirmProductReservationServiceGrpc.ConfirmProductReservationServiceImplBase;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class ConfirmReservationStub extends ConfirmProductReservationServiceImplBase {

    private final ConfirmReservationUseCase useCase;

    @Override
    public void confirmReservation(ConfirmProductReservationRequest request,
        StreamObserver<ConfirmProductReservationResponse> responseObserver) {
        try {

            log.info("[gRPC] confirmReservation request - {}", toJsonString(request));

            ConfirmReservationServiceResponse response = useCase.confirmReservation(
                ConfirmReservationCommand.builder()
                    .productId(request.getProductId())
                    .reserveId(request.getReservationId())
                    .build());

            responseObserver.onNext(ConfirmProductReservationResponse.newBuilder()
                .setResult(response.result())
                .setProductId(response.productId())
                .setSellerId(response.sellerId())
                .setBuyQuantity(response.buyQuantity())
                .build());
            responseObserver.onCompleted();
            log.info("[gRPC] confirmReservation response - {}", toJsonString(response));

        } catch (Exception e) {
            Status status = GrpcUtil.getStatus(e);
            log.info("[gRPC] confirmReservation error - {}", toJsonString(status));

            responseObserver.onError(status
                .withDescription(status.getDescription())
                .asRuntimeException());
        }
    }
}
