package com.product.adapter.in.grpc;

import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.product.application.port.in.FindProductUseCase;
import com.product.application.port.in.UpdateProductQuantityUseCase;
import com.product.application.port.in.command.FindProductCommand;
import com.product.application.port.in.command.UpdateProductQuantityCommand;
import com.product.application.service.find_product.FindProductServiceResponse;
import com.product.application.service.update_product_quantity.UpdateProductQuantityServiceResponse;
import com.product.domain.model.QuantityType;
import com.product.infrastructure.util.GrpcUtil;
import grpc.product.FindProductStubRequest;
import grpc.product.FindProductStubResponse;
import grpc.product.ProductServiceGrpc.ProductServiceImplBase;
import grpc.product.UpdateProductQuantityStubRequest;
import grpc.product.UpdateProductQuantityStubResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.util.StringUtils;

@Slf4j
@GrpcService
@RequiredArgsConstructor
class ProductStub extends ProductServiceImplBase {

    private final FindProductUseCase findProductUseCase;
    private final UpdateProductQuantityUseCase updateProductQuantityUseCase;

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
    public void updateProductQuantity(UpdateProductQuantityStubRequest request,
        StreamObserver<UpdateProductQuantityStubResponse> responseObserver) {
        try {
            log.info("[gRPC] updateProductQuantity request - {}", toJsonString(request));
            validation(request);

            UpdateProductQuantityServiceResponse serviceResponse = updateProductQuantityUseCase
                .updateProductQuantity(UpdateProductQuantityCommand.of(request));

            responseObserver.onNext(serviceResponse.toStubResponse());
            responseObserver.onCompleted();
            log.info("[gRPC] updateProductQuantity response - {}", toJsonString(serviceResponse));

        } catch (Exception e) {
            Status status = GrpcUtil.getStatus(e);
            log.info("[gRPC] updateProductQuantity error - {}", toJsonString(status));

            responseObserver.onError(status
                .withDescription(status.getDescription())
                .asRuntimeException());
        }
    }

    void validation(UpdateProductQuantityStubRequest request) {
        if (request.getQuantity() < 1) {
            throw new IllegalArgumentException("상품 수량은 1 이상 이어야 합니다");
        }
        if (!StringUtils.hasText(request.getQuantityType())) {
            throw new IllegalArgumentException("수정 타입은 필수 값 입니다");
        }
        try {
            QuantityType.valueOf(request.getQuantityType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 수정 타입 입니다");
        }
    }
}
