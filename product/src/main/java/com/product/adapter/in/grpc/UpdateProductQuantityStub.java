package com.product.adapter.in.grpc;

import static com.product.infrastructure.util.GrpcUtil.sendErrorResponse;
import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.product.application.port.in.IncreaseProductQuantityUseCase;
import com.product.application.port.in.command.IncreaseProductQuantityCommand;
import com.product.application.service.Increase_product_quantity.IncreaseProductQuantityServiceResponse;
import grpc.product.UpdateProductQuantityServiceGrpc.UpdateProductQuantityServiceImplBase;
import grpc.product.UpdateProductQuantityStubRequest;
import grpc.product.UpdateProductQuantityStubResponse;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
class UpdateProductQuantityStub extends UpdateProductQuantityServiceImplBase {

    private final String methodName;
    private final IncreaseProductQuantityUseCase useCase;

    UpdateProductQuantityStub(IncreaseProductQuantityUseCase useCase) {
        this.useCase = useCase;
        this.methodName = "updateProductQuantity";
    }

    @Override
    public void updateProductQuantity(
        UpdateProductQuantityStubRequest request,
        StreamObserver<UpdateProductQuantityStubResponse> responseObserver
    ) {
        try {
            log.info("[gRPC] {} request - {}", methodName, toJsonString(request));
            IncreaseProductQuantityServiceResponse response = useCase.update(
                request.getProductId(),
                IncreaseProductQuantityCommand.builder()
                    .quantity(request.getQuantity())
                    .build());

            responseObserver.onNext(UpdateProductQuantityStubResponse.newBuilder()
                .setResult(response.result())
                .build());
            responseObserver.onCompleted();
            log.info("[gRPC] {} response - {}", methodName, toJsonString(response));
            
        } catch (Exception e) {
            sendErrorResponse(e, responseObserver, methodName);
        }
    }
}
