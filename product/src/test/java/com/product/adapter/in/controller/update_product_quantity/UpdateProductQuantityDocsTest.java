package com.product.adapter.in.controller.update_product_quantity;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.product.RestDocsSupport;
import com.product.application.port.in.UpdateProductQuantityUseCase;
import com.product.application.service.update_product_quantity.UpdateProductQuantityServiceResponse;
import com.product.infrastructure.exception.CustomAuthenticationException;
import com.product.infrastructure.exception.CustomAuthorizationException;
import com.product.infrastructure.exception.CustomNotFoundException;
import com.product.infrastructure.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

class UpdateProductQuantityDocsTest extends RestDocsSupport {

    private final UpdateProductQuantityUseCase updateProductQuantityUseCase = mock(
        UpdateProductQuantityUseCase.class);

    @Override
    protected Object initController() {
        return new UpdateProductQuantityController(updateProductQuantityUseCase);
    }

    @Nested
    @DisplayName("[updateProductQuantity] 상품 수량을 수정하는 API")
    class Describe_updateProductQuantity {

        @Test
        @DisplayName("[success] 권한을 가진 사용자가 API 를 요청 했을 때 200코드와 성공 메시지를 응답한다.")
        void updateProductQuantity() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .quantity(10)
                .quantityType("ADD_QUANTITY")
                .build();
            String authorization = "testToken";
            Long productId = 10L;
            given(updateProductQuantityUseCase.updateProductQuantity(any()))
                .willReturn(UpdateProductQuantityServiceResponse.builder()
                    .result(true)
                    .build());

            // when // then
            performDocument(request, productId, authorization, status().isOk(),
                "success",
                "[response] update-product-quantity",
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("응답 데이터"),
                fieldWithPath("data.result").type(JsonFieldType.BOOLEAN)
                    .description("수정 결과")
            );
        }

        @Test
        @DisplayName("[error] 인증받지 않은 사용자가 API 를 요청 했을 때 401코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .quantity(10)
                .quantityType("ADD_QUANTITY")
                .build();
            String authorization = "testToken";
            Long productId = 10L;
            given(updateProductQuantityUseCase.updateProductQuantity(any()))
                .willThrow(new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN));

            // when // then
            performErrorDocument(request, productId, authorization, status().isUnauthorized(),
                "인증받지 않은 사용자 요청");
        }

        @Test
        @DisplayName("[error] 상품 수량 추가 권한이 없는 사용자가 API 를 요청 했을 때 403코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .quantity(10)
                .quantityType("ADD_QUANTITY")
                .build();
            String authorization = "testToken";
            Long productId = 10L;
            given(updateProductQuantityUseCase.updateProductQuantity(any()))
                .willThrow(new CustomAuthorizationException(ErrorCode.ACCESS_DENIED));

            // when // then
            performErrorDocument(request, productId, authorization, status().isForbidden(),
                "상품 수량 추가 권한이 없는 사용자 요청");
        }

        @Test
        @DisplayName("[error] 상품 수량을 음수로 입력한 경우 400 코드와 에러 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .quantity(-1)
                .quantityType("ADD_QUANTITY")
                .build();
            String authorization = "testToken";
            Long productId = 10L;

            // when // then
            performErrorDocument(request, productId, authorization, status().isBadRequest(),
                "상품 수량 음수로 입력");
        }

        @Test
        @DisplayName("[error] 수정할 상품 정보가 없는 경우 404 코드와 에러 메시지를 응답한다.")
        void error4() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .quantity(20)
                .quantityType("ADD_QUANTITY")
                .build();
            String authorization = "testToken";
            Long productId = 10L;
            given(updateProductQuantityUseCase.updateProductQuantity(any()))
                .willThrow(new CustomNotFoundException(ErrorCode.DoesNotExist_PROUCT_INFO));

            // when // then
            performErrorDocument(request, productId, authorization, status().isNotFound(),
                "조회된 상품 정보 없음");
        }

        @Test
        @DisplayName("[error] 유효하지 않은 수정 타입을 입력한 경우 400 코드와 에러 메시지를 응답한다.")
        void error5() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .quantity(20)
                .quantityType("error")
                .build();
            String authorization = "testToken";
            Long productId = 10L;

            // when // then
            performErrorDocument(request, productId, authorization, status().isBadRequest(),
                "유효하지 않은 수정타입 입력");
        }

        @Test
        @DisplayName("[error] 수정타입을 입력하지 않은 경우 400 코드와 에러 메시지를 응답한다.")
        void error6() throws Exception {
            // given
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .quantity(20)
                .build();
            String authorization = "testToken";
            Long productId = 11L;

            // when // then
            performErrorDocument(request, productId, authorization, status().isBadRequest(),
                "유효하지 않은 수정타입 입력");
        }
    }

    private void performDocument(UpdateProductQuantityRequest request, Long productId,
        String authorization,
        ResultMatcher status, String docIdentifier, String responseSchema,
        FieldDescriptor... responseFields)
        throws Exception {

        JsonFieldType quantityType =
            request.quantityType() == null ? JsonFieldType.NULL : JsonFieldType.STRING;

        mockMvc.perform(put("/products/{productId}/quantity", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", authorization)
            )
            .andDo(print())
            .andExpect(status)
            .andDo(MockMvcRestDocumentationWrapper.document(
                    "[register-product-quantity] " + docIdentifier,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Product")
                        .summary("상품 수량 수정 API")
                        .description("상품 수량을 수정하는 API 입니다. <br><br>"
                            + "1. 테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. (요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다) <br>"
                            + "2. 권한을 가진 사용자만 상품 수량을 수정할 수 있습니다."
                            + "3. 상품 수량 추가는 해당 상품을 등록한 사용자만 수정 가능합니다.")
                        .requestFields(
                            fieldWithPath("quantity").type(JsonFieldType.NUMBER)
                                .description("상품수량 (1 이상)"),
                            fieldWithPath("quantityType").type(quantityType)
                                .description("수정 타입 (ADD_QUANTITY, PURCHASE, REFUND")
                        )
                        .responseFields(responseFields)
                        .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                        .requestSchema(Schema.schema("[request] update-product-quantity"))
                        .responseSchema(Schema.schema(responseSchema))
                        .build())
                )
            );
    }

    private void performErrorDocument(UpdateProductQuantityRequest request, Long productId,
        String authorization, ResultMatcher status, String identifier)
        throws Exception {

        performDocument(request, productId, authorization, status, identifier,
            "[response] error",
            fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                .description("상태 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING)
                .description("상태 메시지"),
            fieldWithPath("data").type(JsonFieldType.OBJECT)
                .description("응답 데이터"),
            fieldWithPath("data.errorCode").type(JsonFieldType.NUMBER)
                .description("에러 코드"),
            fieldWithPath("data.errorMessage").type(JsonFieldType.STRING)
                .description("에러 메시지")
        );
    }
}
