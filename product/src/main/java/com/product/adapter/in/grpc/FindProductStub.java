package com.product.adapter.in.grpc;

import static com.common.infrastructure.util.GrpcUtil.getGrpcRequestJson;
import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.common.infrastructure.util.GrpcUtil;
import com.product.application.port.in.FindProductUseCase;
import com.product.application.port.in.command.FindProductCommand;
import com.product.application.service.find_product.FindProductServiceResponse;
import grpc.product.FindProductRequest;
import grpc.product.FindProductResponse;
import grpc.product.FindProductServiceGrpc.FindProductServiceImplBase;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
class FindProductStub extends FindProductServiceImplBase {

    private final FindProductUseCase useCase;


    @Override
    public void findProduct(FindProductRequest request,
        StreamObserver<FindProductResponse> responseObserver) {
        try {

            log.info("[gRPC] findProduct request - {}", getGrpcRequestJson(request));

            FindProductServiceResponse response = useCase.findProduct(FindProductCommand.builder()
                .productId(request.getProductId())
                .isApiCall(false)
                .build());

            responseObserver.onNext(FindProductResponse.newBuilder()
                .setProductId(response.productId())
                .setSellerEmail(response.sellerEmail())
                .setProductName(response.productName())
                .setProductImgUrl(response.productImgUrl())
                .setDescriptionImgUrl(response.descriptionImgUrl())
                .addAllKeywords(new ArrayList<>(response.keywords()))
                .setPrice(response.price())
                .setQuantity(response.quantity())
                .setCategory(response.category().description())
                .setRegDateTime(response.regDateTime())
                .build());
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
