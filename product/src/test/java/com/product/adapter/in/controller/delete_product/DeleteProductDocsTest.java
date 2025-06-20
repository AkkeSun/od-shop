package com.product.adapter.in.controller.delete_product;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.product.RestDocsSupport;
import com.product.adapter.in.controller.delete_product.DeleteProductController;
import com.product.application.port.in.DeleteProductUseCase;
import com.product.application.service.delete_product.DeleteProductServiceResponse;
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

public class DeleteProductDocsTest extends RestDocsSupport {

    DeleteProductUseCase deleteProductUseCase = mock(DeleteProductUseCase.class);

    @Override
    protected Object initController() {
        return new DeleteProductController(deleteProductUseCase);
    }

    @Nested
    @DisplayName("[deleteProduct] 상품을 삭제하는 API")
    class Describe_deleteProduct {

        @Test
        @DisplayName("[success] 판매 권한을 가진 사용자가 API 를 요청 했을 때 200코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            Long productId = 1L;
            String authorization = "testToken";
            when(deleteProductUseCase.deleteProduct(any(), any()))
                .thenReturn(DeleteProductServiceResponse.builder().result(true).build());

            // when then
            performAndDocument(productId, authorization, status().isOk(),
                "[delete-product] success",
                "[response] delete-product",
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("응답 데이터"),
                fieldWithPath("data.result").type(JsonFieldType.BOOLEAN)
                    .description("삭제 결과")
            );
        }

        @Test
        @DisplayName("[error] 인증받지 않은 사용자가 API 를 요청 했을 때 401코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            Long productId = 1L;
            String authorization = "invalidToken";
            when(deleteProductUseCase.deleteProduct(any(), any()))
                .thenThrow(new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN));

            // when then
            performErrorDocument(productId, authorization, status().isUnauthorized(),
                "[delete-product] 인증받지 않은 사용자 요청");
        }

        @Test
        @DisplayName("[error] 상품 등록 권한이 없는 사용자가 API 를 요청 했을 때 403코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            Long productId = 1L;
            String authorization = "invalidToken2";
            when(deleteProductUseCase.deleteProduct(any(), any()))
                .thenThrow(new CustomAuthorizationException(ErrorCode.ACCESS_DENIED));

            // then then
            performErrorDocument(productId, authorization, status().isForbidden(),
                "[delete-product] 권한 없는 사용자 요청");

        }

        @Test
        @DisplayName("[error] 삭제할 상품이 조회디지 않는 경우 404코드와 에러 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            Long productId = 2L;
            String authorization = "testToken";
            when(deleteProductUseCase.deleteProduct(any(), any()))
                .thenThrow(new CustomNotFoundException(ErrorCode.DoesNotExist_PROUCT_INFO));

            // when then
            performErrorDocument(productId, authorization, status().isNotFound(),
                "[delete-product] 조회된 상품 없음");
        }
    }

    private void performAndDocument(Long productId, String authorization,
        ResultMatcher status, String docIdentifier, String responseSchema,
        FieldDescriptor... responseFields)
        throws Exception {

        mockMvc.perform(delete("/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
            )
            .andDo(print())
            .andExpect(status)
            .andDo(MockMvcRestDocumentationWrapper.document(docIdentifier,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Product")
                        .summary("상품 삭제 API")
                        .description("상품을 삭제하는 API 입니다. <br><br>"
                            + "1. 테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. (요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다) <br>"
                            + "2. 해당 상품의 판매자만 상품 삭제가 가능합니다.")
                        .pathParameters(
                            parameterWithName("productId").description("상품 아이디")
                        )
                        .responseFields(responseFields)
                        .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                        .requestSchema(Schema.schema("[request] delete-product"))
                        .responseSchema(Schema.schema(responseSchema))
                        .build())
                )
            );
    }

    private void performErrorDocument(Long productId, String authorization,
        ResultMatcher status, String identifier)
        throws Exception {

        performAndDocument(productId, authorization, status, identifier, "[response] error",
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
