package com.product.adapter.in.grpc;

import com.product.application.port.in.UpdateProductQuantityUseCase;
import grpc.product.UpdateProductQuantityRequest;
import grpc.product.UpdateProductQuantityResponse;
import grpc.product.UpdateProductQuantityStubGrpc.UpdateProductQuantityStubImplBase;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
class UpdateProductQuantityStub extends UpdateProductQuantityStubImplBase {

    private final UpdateProductQuantityUseCase useCase;

    @Override
    public void updateProductQuantity(UpdateProductQuantityRequest request,
        StreamObserver<UpdateProductQuantityResponse> responseObserver) {
        super.updateProductQuantity(request, responseObserver);

        // validation

        // response check

        // result
    }
}
