package com.product.adapter.in.grpc;

import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.product.application.port.in.FindProductUseCase;
import com.product.application.port.in.command.FindProductCommand;
import com.product.application.service.find_product.FindProductServiceResponse;
import com.product.infrastructure.util.GrpcUtil;
import grpc.product.FindProductStubRequest;
import grpc.product.FindProductStubResponse;
import grpc.product.ProductServiceGrpc.ProductServiceImplBase;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
class ProductStub extends ProductServiceImplBase {

    private final FindProductUseCase findProductUseCase;

    @Override
    public void findProduct(FindProductStubRequest request,
        StreamObserver<FindProductStubResponse> responseObserver) {
        try {

            log.info("[gRPC] findProduct request - {}", toJsonString(request));

            FindProductServiceResponse response = findProductUseCase.findProduct(
                FindProductCommand.ofGrpcCall(request.getProductId()));

            responseObserver.onNext(response.toStubResponse());
            responseObserver.onCompleted();
            log.info("[gRPC] findProduct response - {}", toJsonString(response));

        } catch (Exception e) {
            Status status = GrpcUtil.getStatus(e);
            log.info("[gRPC] findProduct error - {}", toJsonString(status));

            responseObserver.onError(status
                .withDescription(status.getDescription())
                .asRuntimeException());
        }
    }
}
