package com.product.adapter.in.controller.find_recommend_product;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
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
import com.product.application.port.in.FindRecommendProductUseCase;
import com.product.application.service.find_recommend_product.FindRecommendProductServiceResponse;
import com.product.domain.model.ProductRecommend;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

public class FindRecommendProductDocsTest extends RestDocsSupport {

    private final FindRecommendProductUseCase findRecommendProductUseCase = mock(
        FindRecommendProductUseCase.class);

    @Override
    protected Object initController() {
        return new FindRecommendProductController(findRecommendProductUseCase);
    }

    @Nested
    @DisplayName("[findRecommendProduct] 추천 상품 목록을 조회하는 API")
    class Describe_findRecommendProduct {

        @Test
        @DisplayName("[success] 조회 권한을 가진 사용자가 필수값을 모두 입력했을 때 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            Mockito.when(findRecommendProductUseCase.findRecommendProductList(any()))
                .thenReturn(FindRecommendProductServiceResponse.builder()
                    .personallyList(List.of(ProductRecommend.builder()
                        .productId(21L)
                        .productName("product1")
                        .sellerEmail("seller1")
                        .productImgUrl("img1")
                        .price(10000)
                        .build()))
                    .popularList(List.of(ProductRecommend.builder()
                        .productId(22L)
                        .productName("product2")
                        .sellerEmail("seller2")
                        .productImgUrl("img2")
                        .price(20000)
                        .build()))
                    .trendList(List.of(ProductRecommend.builder()
                        .productId(23L)
                        .productName("product3")
                        .sellerEmail("seller3")
                        .productImgUrl("img3")
                        .price(30000)
                        .build()))
                    .build());
            FindRecommendProductRequest request = FindRecommendProductRequest.builder()
                .searchDate("20250501")
                .build();
            String token = "Bearer test";

            // when then
            performDocument(status().isOk(), token, request, "success",
                "[response] find-recommend-product",
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("응답 데이터"),
                fieldWithPath("data.personallyList").type(JsonFieldType.ARRAY)
                    .description("사용자 구매 데이터 기반 추천 상품"),
                fieldWithPath("data.popularList").type(JsonFieldType.ARRAY)
                    .description("최다 판매 상품"),
                fieldWithPath("data.trendList").type(JsonFieldType.ARRAY)
                    .description("트랜드 분석 기반 추천 상품"),
                fieldWithPath("data.personallyList[].productId").type(JsonFieldType.NUMBER)
                    .description("상품 아이디"),
                fieldWithPath("data.personallyList[].productName").type(JsonFieldType.STRING)
                    .description("상품명"),
                fieldWithPath("data.personallyList[].sellerEmail").type(JsonFieldType.STRING)
                    .description("판매자 이메일"),
                fieldWithPath("data.personallyList[].productImgUrl").type(JsonFieldType.STRING)
                    .description("상품 이미지 주소"),
                fieldWithPath("data.personallyList[].price").type(JsonFieldType.NUMBER)
                    .description("상품 가격"),
                fieldWithPath("data.popularList[].productId").type(JsonFieldType.NUMBER)
                    .description("상품 아이디"),
                fieldWithPath("data.popularList[].productName").type(JsonFieldType.STRING)
                    .description("상품명"),
                fieldWithPath("data.popularList[].sellerEmail").type(JsonFieldType.STRING)
                    .description("판매자 이메일"),
                fieldWithPath("data.popularList[].productImgUrl").type(JsonFieldType.STRING)
                    .description("상품 이미지 주소"),
                fieldWithPath("data.popularList[].price").type(JsonFieldType.NUMBER)
                    .description("상품 가격"),
                fieldWithPath("data.trendList[].productId").type(JsonFieldType.NUMBER)
                    .description("상품 아이디"),
                fieldWithPath("data.trendList[].productName").type(JsonFieldType.STRING)
                    .description("상품명"),
                fieldWithPath("data.trendList[].sellerEmail").type(JsonFieldType.STRING)
                    .description("판매자 이메일"),
                fieldWithPath("data.trendList[].productImgUrl").type(JsonFieldType.STRING)
                    .description("상품 이미지 주소"),
                fieldWithPath("data.trendList[].price").type(JsonFieldType.NUMBER)
                    .description("상품 가격")
            );
        }
    }

    @Test
    @DisplayName("[error] 검색 날짜를 입력하지 않았을 때 400 코드와 에러 메시지를 응답한다.")
    void error3() throws Exception {
        // given
        FindRecommendProductRequest request = FindRecommendProductRequest.builder()
            .searchDate("")
            .build();
        String authorization = "Bearer test-token";

        // when then
        performErrorDocument(status().isBadRequest(), authorization, request,
            "조회 날짜 미입력");
    }

    @Test
    @DisplayName("[error] 유효하지 않은 조회 날짜를 입력했을 때 400 코드와 에러 메시지를 응답한다.")
    void error4() throws Exception {
        // given
        FindRecommendProductRequest request = FindRecommendProductRequest.builder()
            .searchDate("error")
            .build();
        String authorization = "Bearer test-token";

        // when then
        performErrorDocument(status().isBadRequest(), authorization, request,
            "유효하지 않은 조회 날짜 입력");
    }

    private void performDocument(ResultMatcher status, String token,
        FindRecommendProductRequest request,
        String docIdentifier, String responseSchema, FieldDescriptor... responseFields)
        throws Exception {

        mockMvc.perform(get("/products/recommendations")
                .param("searchDate", request.getSearchDate())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status)
            .andDo(MockMvcRestDocumentationWrapper.document(
                    "[find-recommend-product] " + docIdentifier,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Product")
                        .summary("추천 상품 목록 조회 API")
                        .description("추천 상품을 조회하는 API 입니다. <br><br>"
                            + "1. 최다 판매 상품, 트랜드 분석 기반 추천 상품이 기본 응답되며 인증 토큰 입력시 사용자 구매 데이터 기반 추천 상품이 추가로 응답 됩니다. <br>"
                            + "2. 중복된 추천 상품은 제거되어 응답 됩니다. <br>"
                            + "3. 테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. (요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다) <br>"
                            + "4. 구매 권한을 가진 사용자만 조회할 수 있습니다.")
                        .queryParameters(
                            parameterWithName("searchDate").description("검색 날짜 (yyyyMMdd)")
                        )
                        .requestHeaders(headerWithName("Authorization").description("인증 토큰")
                            .optional())
                        .responseFields(responseFields)
                        .requestSchema(Schema.schema("[request] find-recommend-product"))
                        .responseSchema(Schema.schema(responseSchema))
                        .build()
                    )
                )
            );
    }

    private void performErrorDocument(ResultMatcher status, String token,
        FindRecommendProductRequest request,
        String docIdentifier) throws Exception {

        performDocument(status, token, request, docIdentifier, "[response] error",
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
