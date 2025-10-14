package com.product.adapter.in.controller.update_product;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
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
import com.product.application.port.in.UpdateProductUseCase;
import com.product.application.service.update_product.UpdateProductServiceResponse;
import com.product.domain.model.Category;
import com.product.infrastructure.exception.CustomAuthenticationException;
import com.product.infrastructure.exception.CustomAuthorizationException;
import com.product.infrastructure.exception.CustomNotFoundException;
import com.product.infrastructure.exception.ErrorCode;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

public class UpdateProductDocsTest extends RestDocsSupport {

    UpdateProductUseCase updateProductUseCase = mock(UpdateProductUseCase.class);

    @Override
    protected Object initController() {
        return new UpdateProductController(updateProductUseCase);
    }

    @Test
    @DisplayName("[success] 판매 권한을 가진 사용자가 API 를 요청 했을 때 200코드와 성공 메시지를 응답한다.")
    void success() throws Exception {
        // given
        UpdateProductRequest request = UpdateProductRequest.builder()
            .price(10000L)
            .productOption(Set.of("Option1", "Option2"))
            .keywords(Set.of("Keyword1", "Keyword2"))
            .productName("Updated Product")
            .productImgUrl("http://example.com/image.jpg")
            .descriptionImgUrl("http://example.com/description.jpg")
            .build();
        Long productId = 1L;
        String authorization = "Bearer testToken";

        when(updateProductUseCase.updateProduct(any()))
            .thenReturn(UpdateProductServiceResponse.builder()
                .productId(productId)
                .productName(request.productName())
                .sellerEmail("sellerEmail")
                .productImgUrl(request.productImgUrl())
                .descriptionImgUrl(request.descriptionImgUrl())
                .keywords(request.keywords())
                .price(request.price())
                .category(Category.TOTAL)
                .build());

        // when then
        performAndDocument(request, productId, authorization, status().isOk(),
            "[update-product] success", "[response] update-product",
            fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                .description("상태 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING)
                .description("상태 메시지"),
            fieldWithPath("data").type(JsonFieldType.OBJECT)
                .description("응답 데이터"),
            fieldWithPath("data.productId").type(JsonFieldType.NUMBER)
                .description("수정된 상품 ID"),
            fieldWithPath("data.sellerEmail").type(JsonFieldType.STRING)
                .description("판매자 이메일"),
            fieldWithPath("data.productName").type(JsonFieldType.STRING)
                .description("상품명"),
            fieldWithPath("data.productImgUrl").type(JsonFieldType.STRING)
                .description("상품 이미지 URL"),
            fieldWithPath("data.descriptionImgUrl").type(JsonFieldType.STRING)
                .description("상품 설명 이미지 URL"),
            fieldWithPath("data.keywords").type(JsonFieldType.ARRAY)
                .description("상품 키워드"),
            fieldWithPath("data.price").type(JsonFieldType.NUMBER)
                .description("상품 가격"),
            fieldWithPath("data.category").type(JsonFieldType.STRING)
                .description("상품 카테고리")
        );
    }

    @Test
    @DisplayName("[error] 인증받지 않은 사용자가 API 를 요청 했을 때 401코드와 에러 메시지를 응답한다.")
    void error() throws Exception {
        // given
        UpdateProductRequest request = UpdateProductRequest.builder()
            .price(10000L)
            .productOption(Set.of("Option1", "Option2"))
            .keywords(Set.of("Keyword1", "Keyword2"))
            .productName("Updated Product")
            .productImgUrl("http://example.com/image.jpg")
            .descriptionImgUrl("http://example.com/description.jpg")
            .build();
        Long productId = 1L;
        String authorization = "Bearer testToken";
        given(updateProductUseCase.updateProduct(any())).willThrow(
            new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN));

        // when then
        performErrorDocument(request, productId, authorization, status().isUnauthorized(),
            "[update-product] 인증받지 않은 사용자 요청");
    }

    @Test
    @DisplayName("[error] 상품 등록 권한이 없는 사용자가 API 를 요청 했을 때 403코드와 에러 메시지를 응답한다.")
    void error2() throws Exception {
        // given
        UpdateProductRequest request = UpdateProductRequest.builder()
            .price(10000L)
            .productOption(Set.of("Option1", "Option2"))
            .keywords(Set.of("Keyword1", "Keyword2"))
            .productName("Updated Product")
            .productImgUrl("http://example.com/image.jpg")
            .descriptionImgUrl("http://example.com/description.jpg")
            .build();
        Long productId = 1L;
        String authorization = "Bearer testToken";
        given(updateProductUseCase.updateProduct(any())).willThrow(
            new CustomAuthorizationException(ErrorCode.ACCESS_DENIED));

        // when then
        performErrorDocument(request, productId, authorization, status().isForbidden(),
            "[update-product] 상품 수정 권한이 없는 사용자 요청");
    }

    @Test
    @DisplayName("[error] 판매 권한을 가진 사용자가 상품명을 50자 이상으로 입력했을 때400코드와 에러 메시지를 응답한다.")
    void error3() throws Exception {
        // given
        UpdateProductRequest request = UpdateProductRequest.builder()
            .price(10000L)
            .productOption(Set.of("Option1", "Option2"))
            .keywords(Set.of("Keyword1", "Keyword2"))
            .productName("test".repeat(13))
            .productImgUrl("http://example.com/image.jpg")
            .descriptionImgUrl("http://example.com/description.jpg")
            .build();
        Long productId = 1L;
        String authorization = "Bearer testToken";

        // when then
        performErrorDocument(request, productId, authorization, status().isBadRequest(),
            "[update-product] 상품명 입력 사이즈 초과");
    }

    @Test
    @DisplayName("[error] 판매 권한을 가진 사용자가 50자 이상의 상품 이미지를 입력했을 때 400코드와 에러 메시지를 응답한다.")
    void error4() throws Exception {
        // given
        UpdateProductRequest request = UpdateProductRequest.builder()
            .price(10000L)
            .productOption(Set.of("Option1", "Option2"))
            .keywords(Set.of("Keyword1", "Keyword2"))
            .productName("Updated Product")
            .productImgUrl("test".repeat(13))
            .descriptionImgUrl("http://example.com/description.jpg")
            .build();
        Long productId = 1L;
        String authorization = "Bearer testToken";

        // when then
        performErrorDocument(request, productId, authorization, status().isBadRequest(),
            "[update-product] 상품 이미지 입력 사이즈 초과");
    }

    @Test
    @DisplayName("[error] 판매 권한을 가진 사용자가 50자 이상의 상품 설명 이미지를 입력했을 때 400코드와 에러 메시지를 응답한다.")
    void error5() throws Exception {
        // given
        UpdateProductRequest request = UpdateProductRequest.builder()
            .price(10000L)
            .productOption(Set.of("Option1", "Option2"))
            .keywords(Set.of("Keyword1", "Keyword2"))
            .productName("Updated Product")
            .productImgUrl("http://example.com/image.jpg")
            .descriptionImgUrl("test".repeat(13))
            .build();
        Long productId = 1L;
        String authorization = "Bearer testToken";

        // when then
        performErrorDocument(request, productId, authorization, status().isBadRequest(),
            "[update-product] 상품 설명 이미지 입력 사이즈 초과");
    }

    @Test
    @DisplayName("[error] 조회된 상품 정보가 없을 때 404코드와 에러 메시지를 응답한다.")
    void error6() throws Exception {
        // given
        UpdateProductRequest request = UpdateProductRequest.builder()
            .price(10000L)
            .productOption(Set.of("Option1", "Option2"))
            .keywords(Set.of("Keyword1", "Keyword2"))
            .productName("Updated Product")
            .productImgUrl("http://example.com/image.jpg")
            .descriptionImgUrl("http://example.com/description.jpg")
            .build();
        Long productId = 2L;
        String authorization = "Bearer testToken";
        given(updateProductUseCase.updateProduct(any())).willThrow(
            new CustomNotFoundException(ErrorCode.DoesNotExist_PRODUCT_INFO));

        // when then
        performErrorDocument(request, productId, authorization, status().isNotFound(),
            "[update-product] 조회된 상품 없음");
    }

    private void performAndDocument(UpdateProductRequest request, Long productId,
        String authorization, ResultMatcher status, String docIdentifier, String responseSchema,
        FieldDescriptor... responseFields)
        throws Exception {

        mockMvc.perform(put("/products/{productId} ", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", authorization)
            )
            .andDo(print())
            .andExpect(status)
            .andDo(MockMvcRestDocumentationWrapper.document(docIdentifier,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Product")
                        .summary("상품 수정 API")
                        .description("상품을 수정하는 API 입니다. <br><br>"
                            + "1. 테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. (요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다) <br>"
                            + "2. 해당 상품의 판매자만 상품을 수정할 수 있습니다. <br>"
                            + "3. 수정이 필요한 정보만 입력해야 합니다.")
                        .pathParameters(
                            parameterWithName("productId").description("상품 아이디")
                        )
                        .requestFields(
                            fieldWithPath("productName").type(JsonFieldType.STRING)
                                .description("상품명 (50자 이하)").optional(),
                            fieldWithPath("price").type(JsonFieldType.NUMBER)
                                .description("상품 금액").optional(),
                            fieldWithPath("productImgUrl").type(JsonFieldType.STRING)
                                .description("상품 이미지 (50자 이하)").optional(),
                            fieldWithPath("descriptionImgUrl").type(JsonFieldType.STRING)
                                .description("상품 설명 이미지 (50자 이하)").optional(),
                            fieldWithPath("productOption").type(JsonFieldType.ARRAY)
                                .description("상품 옵션").optional(),
                            fieldWithPath("keywords").type(JsonFieldType.ARRAY)
                                .description("상품 키워드").optional()
                        )
                        .responseFields(responseFields)
                        .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                        .requestSchema(Schema.schema("[request] update-product"))
                        .responseSchema(Schema.schema(responseSchema))
                        .build())
                )
            );
    }

    private void performErrorDocument(UpdateProductRequest request, Long productId,
        String authorization, ResultMatcher status, String identifier)
        throws Exception {

        performAndDocument(request, productId, authorization, status, identifier,
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
