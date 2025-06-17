package com.product.adapter.in.register_product;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
import com.product.application.port.in.RegisterProductUseCase;
import com.product.application.service.register_product.RegisterProductServiceResponse;
import com.product.domain.model.Category;
import com.product.infrastructure.exception.CustomAuthenticationException;
import com.product.infrastructure.exception.CustomAuthorizationException;
import com.product.infrastructure.exception.ErrorCode;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

public class RegisterProductDocsTest extends RestDocsSupport {

    private final RegisterProductUseCase registerProductUseCase
        = mock(RegisterProductUseCase.class);

    @Override
    protected Object initController() {
        return new RegisterProductController(registerProductUseCase);
    }

    @Nested
    @DisplayName("[registerProduct] 상품을 등록하는 API")
    class Describe_registerProduct {

        @Test
        @DisplayName("[success] 필수값을 모두 입력하여 API 를 요청 했을 때 200코드와 성공 메시지를 응답한다.")
        void success() throws Exception {

            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .keywords(Set.of("키워드1", "키워드2"))
                .build();
            RegisterProductServiceResponse response = RegisterProductServiceResponse.builder()
                .productId(12345L)
                .sellerEmail("od@test.com")
                .productName(request.productName())
                .productImgUrl(request.productImgUrl())
                .descriptionImgUrl(request.descriptionImgUrl())
                .price(request.price())
                .quantity(request.quantity())
                .category(Category.valueOf(request.category()))
                .keywords(request.keywords())
                .productOption(request.productOption())
                .build();
            String authorization = "testToken";
            given(registerProductUseCase.registerProduct(any())).willReturn(response);

            // when then
            performAndDocument(request,authorization, status().isOk(), "[register-product] success",
                "[response] register-product",
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("응답 데이터"),
                fieldWithPath("data.productId").type(JsonFieldType.NUMBER)
                    .description("상품 코드"),
                fieldWithPath("data.sellerEmail").type(JsonFieldType.STRING)
                    .description("판매자 이메일"),
                fieldWithPath("data.productName").type(JsonFieldType.STRING)
                    .description("판매자명"),
                fieldWithPath("data.productImgUrl").type(JsonFieldType.STRING)
                    .description("상품 이미지"),
                fieldWithPath("data.descriptionImgUrl").type(JsonFieldType.STRING)
                    .description("상품 설명 이미지"),
                fieldWithPath("data.keywords").type(JsonFieldType.ARRAY)
                    .description("상품 키워드"),
                fieldWithPath("data.productOption").type(JsonFieldType.ARRAY)
                    .description("상품 옵션"),
                fieldWithPath("data.price").type(JsonFieldType.NUMBER)
                    .description("상품 금액"),
                fieldWithPath("data.quantity").type(JsonFieldType.NUMBER)
                    .description("상품 수량"),
                fieldWithPath("data.category").type(JsonFieldType.STRING)
                    .description("상품 카테고리"));
        }

        @Test
        @DisplayName("[error] 인증받지 않은 사용자가 API 를 요청 했을 때 401코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .keywords(Set.of("키워드1", "키워드2"))
                .build();
            String authorization = "testToken";
            given(registerProductUseCase.registerProduct(any())).willThrow(
                new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN));

            // when then
            performErrorDocument(request, authorization, status().isUnauthorized(),
                "[register-product] 인증받지 않은 사용자 요청");
        }

        @Test
        @DisplayName("[error] 상품 등록 권한이 없는 사용자가 API 를 요청 했을 때 403코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .keywords(Set.of("키워드1", "키워드2"))
                .build();
            String authorization = "testToken";
            given(registerProductUseCase.registerProduct(any())).willThrow(
                new CustomAuthorizationException(ErrorCode.ACCESS_DENIED));

            // when then
            performErrorDocument(request, authorization, status().isForbidden(),
                "[register-product] 상품 등록 권한이 없는 사용자 요청");
        }

        @Test
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품명을 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName(null)
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .keywords(Set.of("키워드1", "키워드2"))
                .build();
            String authorization = "testToken";

            // when then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "[register-product] 상품명 미입력");
        }

        @Test
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품명을 50자 이상으로 입력했을 때400코드와 에러 메시지를 응답한다.")
        void error4() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("test".repeat(13))
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .keywords(Set.of("키워드1", "키워드2"))
                .build();
            String authorization = "testToken";

            // when then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "[register-product] 상품명 입력 사이즈 초과");
        }

        @Test
        @DisplayName("[error] 판매 권한을 가진 사용자가 카테고리를 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error5() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category(null)
                .price(10000)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .keywords(Set.of("키워드1", "키워드2"))
                .build();
            String authorization = "testToken";

            // when then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "[register-product] 카테고리 미입력");
        }

        @Test
        @DisplayName("[error] 판매 권한을 가진 사용자가 유효하지 않은 카테고리를 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error6() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("unknown")
                .price(10000)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .keywords(Set.of("키워드1", "키워드2"))
                .build();
            String authorization = "testToken";

            // when then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "[register-product] 유효하지 않은 카테고리 입력");
        }

        @Test
        @DisplayName("[error] 판매 권한을 가진 사용자가 1미만의 금액을 입력했을 떄 400코드와 에러 메시지를 응답한다.")
        void error7() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("unknown")
                .price(-1)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .keywords(Set.of("키워드1", "키워드2"))
                .build();
            String authorization = "testToken";

            // when then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "[register-product] 1미만의 상품 금액 입력");
        }

        @Test
        @DisplayName("[error] 판매 권한을 가진 사용자가 20 미만의 상품 수량을 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error8() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("unknown")
                .price(10000)
                .quantity(10)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .keywords(Set.of("키워드1", "키워드2"))
                .build();
            String authorization = "testToken";

            // when then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "[register-product] 20 미만의 상품 수량 입력");
        }

        @Test
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 이미지를 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error9() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(20)
                .productImgUrl(null)
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .keywords(Set.of("키워드1", "키워드2"))
                .build();
            String authorization = "testToken";

            // when then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "[register-product] 상품 이미지 미입력");
        }

        @Test
        @DisplayName("[error] 판매 권한을 가진 사용자가 50자 이상의 상품 이미지를 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error10() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(20)
                .productImgUrl("test".repeat(13))
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .keywords(Set.of("키워드1", "키워드2"))
                .build();
            String authorization = "testToken";

            // when then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "[register-product] 상품 이미지 입력 사이즈 초과");
        }

        @Test
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 설명 이미지를 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error11() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(20)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl(null)
                .productOption(Set.of("옵션1", "옵션2"))
                .keywords(Set.of("키워드1", "키워드2"))
                .build();
            String authorization = "testToken";

            // when then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "[register-product] 상품 설명 이미지 미입력");
        }

        @Test
        @DisplayName("[error] 판매 권한을 가진 사용자가 50자 이상의 상품 설명 이미지를 입력했을 때 400코드와 에러 메시지를 응답한다.")
        void error12() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(20)
                .descriptionImgUrl("test".repeat(13))
                .descriptionImgUrl(null)
                .productOption(Set.of("옵션1", "옵션2"))
                .keywords(Set.of("키워드1", "키워드2"))
                .build();
            String authorization = "testToken";

            // when then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "[register-product] 상품 설명 이미지 입력 사이즈 초과");
        }

        @Test
        @DisplayName("[error] 판매 권한을 가진 사용자가 상품 옵션을 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error13() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(20)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(null)
                .keywords(Set.of("키워드1", "키워드2"))
                .build();
            String authorization = "testToken";

            // when then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "[register-product] 상품 옵션 미입력");
        }

        @Test
        @DisplayName("[error] 판매 권한을 가진 사용자가 키워드를 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
        void error14() throws Exception {
            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(20)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .productOption(Set.of("옵션1", "옵션2"))
                .keywords(null)
                .build();
            String authorization = "testToken";

            // when then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "[register-product] 상품 키워드 미입력");
        }
    }

    private void performAndDocument(RegisterProductRequest request, String authorization,
        ResultMatcher status, String docIdentifier, String responseSchema,
        FieldDescriptor... responseFields)
        throws Exception {

        JsonFieldType productNameType =
            request.productName() == null ? JsonFieldType.NULL : JsonFieldType.STRING;
        JsonFieldType categoryType =
            request.category() == null ? JsonFieldType.NULL : JsonFieldType.STRING;
        JsonFieldType productImgUrlType =
            request.productImgUrl() == null ? JsonFieldType.NULL : JsonFieldType.STRING;
        JsonFieldType descriptionImgUrlType =
            request.descriptionImgUrl() == null ? JsonFieldType.NULL : JsonFieldType.STRING;
        JsonFieldType productOptionType =
            request.productOption() == null ? JsonFieldType.NULL : JsonFieldType.ARRAY;
        JsonFieldType keywordsType =
            request.keywords() == null ? JsonFieldType.NULL : JsonFieldType.ARRAY;

        mockMvc.perform(post("/products")
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
                        .summary("상품 등록 API")
                        .description("상품을 등록하는 API 입니다. <br><br>"
                            + "1. 테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. (요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다) <br>"
                            + "2. 판매자 권한을 가진 사용자만 상품을 등록할 수 있습니다.")
                        .requestFields(
                            fieldWithPath("productName").type(productNameType)
                                .description("상품명 (50자 이하)"),
                            fieldWithPath("category").type(categoryType)
                                .description(
                                    "카테고리 (ELECTRONICS, FASHION, DIGITAL, HOME_APPLIANCES, BEAUTY, BOOKS, SPORTS, FOOD, TOYS, FURNITURE, AUTOMOTIVE, HEALTH, TOTAL)"),
                            fieldWithPath("price").type(JsonFieldType.NUMBER)
                                .description("상품 금액"),
                            fieldWithPath("quantity").type(JsonFieldType.NUMBER)
                                .description("상품 수량 (20 이상)"),
                            fieldWithPath("productImgUrl").type(productImgUrlType)
                                .description("상품 이미지 (50자 이하)"),
                            fieldWithPath("descriptionImgUrl").type(descriptionImgUrlType)
                                .description("상품 설명 이미지 (50자 이하)"),
                            fieldWithPath("productOption").type(productOptionType)
                                .description("상품 옵션"),
                            fieldWithPath("keywords").type(keywordsType)
                                .description("상품 키워드")
                        )
                        .responseFields(responseFields)
                        .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                        .requestSchema(Schema.schema("[request] register-product"))
                        .responseSchema(Schema.schema(responseSchema))
                        .build())
                )
            );
    }

    private void performErrorDocument(RegisterProductRequest request, String authorization,
        ResultMatcher status, String identifier)
        throws Exception {

        performAndDocument(request, authorization, status, identifier, "[response] error",
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
