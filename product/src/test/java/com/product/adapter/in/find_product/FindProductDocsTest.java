package com.product.adapter.in.find_product;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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
import com.product.application.port.in.FindProductUseCase;
import com.product.application.service.find_product.FindProductServiceResponse;
import com.product.domain.model.Category;
import com.product.infrastructure.exception.CustomNotFoundException;
import com.product.infrastructure.exception.ErrorCode;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

public class FindProductDocsTest extends RestDocsSupport {

    private final FindProductUseCase findProductUseCase = mock(FindProductUseCase.class);

    @Override
    protected Object initController() {
        return new FindProductController(findProductUseCase);
    }
    @Nested
    @DisplayName("[findProduct] 상품을 조회하는 API")
    class Describe_findProduct {

        @Test
        @DisplayName("[success] 조회된 정보가 있는 경우 상품 정보를 응답한다.")
        void success() throws Exception {
            // given
            Long productId = 1L;
            FindProductServiceResponse serviceResponse = FindProductServiceResponse.builder()
                .productId(productId)
                .sellerEmail("test@gmail.com")
                .productName("Test Product")
                .category(Category.AUTOMOTIVE)
                .price(10000)
                .quantity(30)
                .productImgUrl("http://example.com/product.jpg")
                .descriptionImgUrl("http://example.com/description.jpg")
                .productOption(Set.of("Option1", "Option2"))
                .keywords(Set.of("Keyword1", "Keyword2"))
                .regDateTime("2025-05-01 12:00:00")
                .build();
            when(findProductUseCase.findProduct(productId)).thenReturn(serviceResponse);

            // when then
            performAndDocument(productId, status().isOk(),"[find-product] success",
                "[response] find-product",
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data.productId").type(JsonFieldType.NUMBER)
                    .description("상품 ID"),
                fieldWithPath("data.productName").type(JsonFieldType.STRING)
                    .description("상품 이름"),
                fieldWithPath("data.sellerEmail").type(JsonFieldType.STRING)
                    .description("판매자 이메일"),
                fieldWithPath("data.category").type(JsonFieldType.STRING)
                    .description("카테고리"),
                fieldWithPath("data.price").type(JsonFieldType.NUMBER)
                    .description("가격"),
                fieldWithPath("data.quantity").type(JsonFieldType.NUMBER)
                    .description("수량"),
                fieldWithPath("data.productImgUrl").type(JsonFieldType.STRING)
                    .description("상품 이미지 URL"),
                fieldWithPath("data.descriptionImgUrl").type(JsonFieldType.STRING)
                    .description("설명 이미지 URL"),
                fieldWithPath("data.productOption").type(JsonFieldType.ARRAY)
                    .description("상품 옵션 목록"),
                fieldWithPath("data.keywords").type(JsonFieldType.ARRAY)
                    .description("키워드 목록"),
                fieldWithPath("data.regDateTime").type(JsonFieldType.STRING)
                    .description("등록 일시")
            );
        }

        @Test
        @DisplayName("[error] 조회된 정보가 없는 경우 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            Long productId = 99L;
            when(findProductUseCase.findProduct(productId))
                .thenThrow(new CustomNotFoundException(ErrorCode.DoesNotExist_PROUCT_INFO));

            // when then
            performErrorDocument(productId, status().isNotFound(), "[find-product] 조회된 상품 정보 없음");
        }
    }

    private void performAndDocument(Long productId, ResultMatcher status,  String docIdentifier,
        String responseSchema, FieldDescriptor... responseFields)
        throws Exception {

        mockMvc.perform(get("/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status)
            .andDo(MockMvcRestDocumentationWrapper.document(docIdentifier,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Product")
                        .summary("상품 조회 API")
                        .description("상품을 조회하는 API 입니다.")
                        .pathParameters(
                            parameterWithName("productId").description("상품 ID")
                        )
                        .responseFields(responseFields)
                        .requestSchema(Schema.schema("[request] find-product"))
                        .responseSchema(Schema.schema(responseSchema))
                        .build())
                )
            );
    }

    private void performErrorDocument(Long productId, ResultMatcher status, String identifier)
        throws Exception {

        performAndDocument(productId, status, identifier, "[response] error",
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
