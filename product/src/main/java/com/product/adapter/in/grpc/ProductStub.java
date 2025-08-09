package com.product.adapter.in.grpc;

import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.product.application.port.in.FindProductUseCase;
import com.product.application.port.in.ReserveProductUseCase;
import com.product.application.port.in.command.FindProductCommand;
import com.product.application.port.in.command.ReserveProductCommand;
import com.product.application.service.find_product.FindProductServiceResponse;
import com.product.application.service.reserve_product.ReserveProductServiceResponse;
import com.product.infrastructure.util.GrpcUtil;
import grpc.product.FindProductStubRequest;
import grpc.product.FindProductStubResponse;
import grpc.product.ProductServiceGrpc.ProductServiceImplBase;
import grpc.product.ReserveProductStubRequest;
import grpc.product.ReserveProductStubResponse;
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
    private final ReserveProductUseCase reserveProductUseCase;

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

    @Override
    public void reserveProduct(ReserveProductStubRequest request,
        StreamObserver<ReserveProductStubResponse> responseObserver) {
        try {

            log.info("[gRPC] reserveProduct request - {}", toJsonString(request));
            ReserveProductServiceResponse response = reserveProductUseCase.reserve(
                ReserveProductCommand.of(request));

            responseObserver.onNext(response.toStubResponse());
            responseObserver.onCompleted();
            log.info("[gRPC] reserveProduct response - {}", toJsonString(response));

        } catch (Exception e) {
            Status status = GrpcUtil.getStatus(e);
            log.info("[gRPC] reserveProduct error - {}", toJsonString(status));

            responseObserver.onError(status
                .withDescription(status.getDescription())
                .asRuntimeException());
        }
    }
}
