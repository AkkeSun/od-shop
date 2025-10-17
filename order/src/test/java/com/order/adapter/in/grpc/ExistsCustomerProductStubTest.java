package com.order.adapter.in.grpc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.order.applicatoin.port.in.ExistsCustomerOrderUseCase;
import com.order.applicatoin.service.exists_customer_order.ExistsCustomerOrderServiceResponse;
import grpc.product.ExistsCustomerProductRequest;
import grpc.product.ExistsCustomerProductResponse;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ExistsCustomerProductStubTest {

    @Mock
    private ExistsCustomerOrderUseCase useCase;

    @Mock
    private StreamObserver<ExistsCustomerProductResponse> responseObserver;

    @InjectMocks
    private ExistsCustomerProductStub stub;

    @Nested
    @DisplayName("[exists] 고객의 상품 주문 존재 여부를 확인하는 gRPC 메소드")
    class Describe_exists {

        @Test
        @DisplayName("[success] 고객의 주문이 존재하면 true를 반환한다")
        void success() {
            // given
            ExistsCustomerProductRequest request = ExistsCustomerProductRequest.newBuilder()
                .setCustomerId(1L)
                .setProductId(100L)
                .build();

            ExistsCustomerOrderServiceResponse mockResponse = ExistsCustomerOrderServiceResponse.of(true);

            given(useCase.exists(any())).willReturn(mockResponse);

            // when
            stub.exists(request, responseObserver);

            // then
            verify(useCase).exists(any());
            verify(responseObserver).onNext(any(ExistsCustomerProductResponse.class));
            verify(responseObserver).onCompleted();
        }

        @Test
        @DisplayName("[success] 고객의 주문이 없으면 false를 반환한다")
        void success_notExists() {
            // given
            ExistsCustomerProductRequest request = ExistsCustomerProductRequest.newBuilder()
                .setCustomerId(1L)
                .setProductId(100L)
                .build();

            ExistsCustomerOrderServiceResponse mockResponse = ExistsCustomerOrderServiceResponse.of(false);

            given(useCase.exists(any())).willReturn(mockResponse);

            // when
            stub.exists(request, responseObserver);

            // then
            verify(useCase).exists(any());
            verify(responseObserver).onNext(any(ExistsCustomerProductResponse.class));
            verify(responseObserver).onCompleted();
        }

        @Test
        @DisplayName("[error] UseCase에서 예외 발생 시 에러 응답을 보낸다")
        void error_useCaseException() {
            // given
            ExistsCustomerProductRequest request = ExistsCustomerProductRequest.newBuilder()
                .setCustomerId(1L)
                .setProductId(100L)
                .build();

            given(useCase.exists(any())).willThrow(new RuntimeException("Database error"));

            // when
            stub.exists(request, responseObserver);

            // then
            verify(useCase).exists(any());
            verify(responseObserver).onError(any());
        }
    }
}
