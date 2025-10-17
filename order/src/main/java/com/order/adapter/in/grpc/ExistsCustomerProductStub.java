package com.order.adapter.in.grpc;

import static com.order.infrastructure.util.GrpcUtil.sendErrorResponse;
import static com.order.infrastructure.util.JsonUtil.toJsonString;

import com.order.application.port.in.ExistsCustomerOrderUseCase;
import com.order.application.port.in.command.ExistsCustomerOrderCommand;
import com.order.application.service.exists_customer_order.ExistsCustomerOrderServiceResponse;
import grpc.product.ExistsCustomerProductRequest;
import grpc.product.ExistsCustomerProductResponse;
import grpc.product.ExistsCustomerProductServiceGrpc.ExistsCustomerProductServiceImplBase;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
class ExistsCustomerProductStub extends ExistsCustomerProductServiceImplBase {

    private final String methodName;
    private final ExistsCustomerOrderUseCase useCase;

    ExistsCustomerProductStub(ExistsCustomerOrderUseCase useCase) {
        this.useCase = useCase;
        this.methodName = "existsCustomerProduct";
    }

    @Override
    public void exists(
        ExistsCustomerProductRequest request,
        StreamObserver<ExistsCustomerProductResponse> responseObserver
    ) {
        try {
            log.info("[gRPC] {} request - {}", methodName, toJsonString(request));
            ExistsCustomerOrderServiceResponse response = useCase.exists(
                ExistsCustomerOrderCommand.builder()
                    .productId(request.getProductId())
                    .customerId(request.getCustomerId())
                    .build());

            responseObserver.onNext(ExistsCustomerProductResponse.newBuilder()
                .setExists(response.exists())
                .build());
            responseObserver.onCompleted();
            log.info("[gRPC] {} response - {}", methodName, toJsonString(response));

        } catch (Exception e) {
            sendErrorResponse(e, responseObserver, methodName);
        }
    }
}
