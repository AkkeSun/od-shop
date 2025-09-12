package com.order.adapter.in.grpc;

import static com.order.infrastructure.util.JsonUtil.toJsonString;

import com.order.applicatoin.port.in.ExistsCustomerOrderUseCase;
import com.order.applicatoin.port.in.command.ExistsCustomerOrderCommand;
import com.order.applicatoin.service.exists_customer_order.ExistsCustomerOrderServiceResponse;
import com.order.infrastructure.util.GrpcUtil;
import grpc.product.ExistsCustomerProductRequest;
import grpc.product.ExistsCustomerProductResponse;
import grpc.product.ExistsCustomerProductServiceGrpc.ExistsCustomerProductServiceImplBase;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
class ExistsCustomerProductStub extends ExistsCustomerProductServiceImplBase {

    private final ExistsCustomerOrderUseCase useCase;

    @Override
    public void exists(
        ExistsCustomerProductRequest request,
        StreamObserver<ExistsCustomerProductResponse> responseObserver
    ) {
        try {
            log.info("[gRPC] existsCustomerProduct request - {}", toJsonString(request));
            ExistsCustomerOrderServiceResponse response = useCase.exists(
                ExistsCustomerOrderCommand.builder()
                    .productId(request.getProductId())
                    .customerId(request.getCustomerId())
                    .build());

            responseObserver.onNext(ExistsCustomerProductResponse.newBuilder()
                .setExists(response.exists())
                .build());
            responseObserver.onCompleted();
            log.info("[gRPC] existsCustomerProduct response - {}", toJsonString(response));

        } catch (Exception e) {
            Status status = GrpcUtil.getStatus(e);
            log.info("[gRPC] existsCustomerProduct error - {}", toJsonString(status));

            responseObserver.onError(status
                .withDescription(status.getDescription())
                .asRuntimeException());
        }
    }
}
