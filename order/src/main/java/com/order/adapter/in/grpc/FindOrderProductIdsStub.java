package com.order.adapter.in.grpc;

import static com.common.infrastructure.util.GrpcUtil.getGrpcRequestJson;
import static com.common.infrastructure.util.GrpcUtil.sendErrorResponse;
import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.order.application.port.in.FindOrderProductIdsUseCase;
import com.order.application.port.in.command.FindOrderProductIdsCommand;
import grpc.product.FindOrderProductIdsRequest;
import grpc.product.FindOrderProductIdsResponse;
import grpc.product.FindOrderProductIdsServiceGrpc.FindOrderProductIdsServiceImplBase;
import io.grpc.stub.StreamObserver;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
class FindOrderProductIdsStub extends FindOrderProductIdsServiceImplBase {

    private final String methodName;
    private final FindOrderProductIdsUseCase useCase;

    FindOrderProductIdsStub(FindOrderProductIdsUseCase useCase) {
        this.useCase = useCase;
        this.methodName = "findIds";
    }

    @Override
    public void findIds(
        FindOrderProductIdsRequest request,
        StreamObserver<FindOrderProductIdsResponse> responseObserver
    ) {
        try {
            log.info("[gRPC] {} request - {}", methodName, getGrpcRequestJson(request));
            List<Long> response = useCase.findOrderProductIds(
                FindOrderProductIdsCommand.builder()
                    .customerId(request.getCustomerId())
                    .limit(request.getLimit())
                    .build());

            responseObserver.onNext(FindOrderProductIdsResponse.newBuilder()
                .addAllProductIds(response)
                .build());
            responseObserver.onCompleted();

            log.info("[gRPC] {} response - {}", methodName, toJsonString(response));
        } catch (Exception e) {
            sendErrorResponse(e, responseObserver, methodName);
        }
    }
}
