package com.product.adapter.in.controller.find_review_list;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
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
import com.product.application.port.in.FindReviewListUseCase;
import com.product.application.service.find_review_list.FindReviewListServiceResponse;
import com.product.application.service.find_review_list.FindReviewListServiceResponse.FindReviewListServiceResponseItem;
import com.common.infrastructure.exception.CustomNotFoundException;
import com.common.infrastructure.exception.ErrorCode;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

public class FindReviewListDocsTest extends RestDocsSupport {

    FindReviewListUseCase findReviewListUseCase = mock(FindReviewListUseCase.class);

    @Override
    protected Object initController() {
        return new FindReviewListController(findReviewListUseCase);
    }

    @Nested
    @DisplayName("[findReviewList] 리뷰를 조회하는 API")
    class Describe_findReviewList {

        @Test
        @DisplayName("[success] 조회된 리뷰가 있을 때 리뷰를 응답한다.")
        void success() throws Exception {
            // given
            Long productId = 1L;
            FindReviewListRequest request = FindReviewListRequest.builder()
                .page(0)
                .size(10)
                .build();
            when(findReviewListUseCase.findReviewList(any()))
                .thenReturn(FindReviewListServiceResponse.builder()
                    .productId(productId)
                    .page(0)
                    .size(10)
                    .reviewCount(2)
                    .reviews(List.of(FindReviewListServiceResponseItem.builder()
                            .review("좋아요")
                            .customerEmail("od@gmail.com")
                            .build(),
                        FindReviewListServiceResponseItem.builder()
                            .review("굿굿")
                            .customerEmail("exg@gmail.com")
                            .build()))
                    .build());

            // when then
            performDocument(request, productId, status().isOk(), "success", "find-review-list",
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("응답 데이터"),
                fieldWithPath("data.productId").type(JsonFieldType.NUMBER)
                    .description("상품 ID"),
                fieldWithPath("data.page").type(JsonFieldType.NUMBER)
                    .description("조회 페이지"),
                fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                    .description("조회 사이즈"),
                fieldWithPath("data.reviewCount").type(JsonFieldType.NUMBER)
                    .description("조회된 리뷰 수"),
                fieldWithPath("data.reviews[]").type(JsonFieldType.ARRAY)
                    .description("리뷰 목록"),
                fieldWithPath("data.reviews[].review").type(JsonFieldType.STRING)
                    .description("리뷰 내용"),
                fieldWithPath("data.reviews[].customerEmail").type(JsonFieldType.STRING)
                    .description("리뷰 작성자")
            );
        }

        @Test
        @DisplayName("[success] 조회된 리뷰에 해당하는 상품 정보가 없을때 예외를 응답한다.")
        void error2() throws Exception {
            // given
            Long productId = 3L;
            FindReviewListRequest request = FindReviewListRequest.builder()
                .page(0)
                .size(10)
                .build();
            when(findReviewListUseCase.findReviewList(any()))
                .thenThrow(new CustomNotFoundException(ErrorCode.DoesNotExist_PRODUCT_INFO));

            // when then
            performErrorDocument(request, productId, status().isNotFound(), "조회된 상품 정보 없음");
        }
    }

    private void performDocument(FindReviewListRequest request, Long productId,
        ResultMatcher status, String docIdentifier,
        String responseSchema, FieldDescriptor... responseFields)
        throws Exception {

        mockMvc.perform(get("/products/{productId}/reviews", productId)
                .queryParam("page", String.valueOf(request.getPage()))
                .queryParam("size", String.valueOf(request.getSize())))
            .andDo(print())
            .andExpect(status)
            .andDo(MockMvcRestDocumentationWrapper.document("[find-review-list] " + docIdentifier,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Review")
                        .summary("리뷰 조회 API")
                        .description("리뷰를 조회하는 API 입니다.")
                        .pathParameters(
                            parameterWithName("productId").description("상품 ID")
                        )
                        .queryParameters(
                            parameterWithName("page").description("페이지 번호 (기본값 :0)").optional(),
                            parameterWithName("size").description("페이지당 리뷰 개수 (기본값: 10)").optional()
                        )
                        .responseFields(responseFields)
                        .requestSchema(Schema.schema("[request] find-review-list"))
                        .responseSchema(Schema.schema("[response] " + responseSchema))
                        .build())
                )
            );
    }

    private void performErrorDocument(FindReviewListRequest request, Long productId,
        ResultMatcher status, String identifier) throws Exception {

        performDocument(request, productId, status, identifier, "error",
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
