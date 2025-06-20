package com.product.adapter.in.controller.find_comment_list;

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
import com.product.application.port.in.FindCommentListUseCase;
import com.product.application.service.find_comment_list.FindCommentListServiceResponse;
import com.product.infrastructure.exception.CustomNotFoundException;
import com.product.infrastructure.exception.ErrorCode;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

public class FindCommentListDocsTest extends RestDocsSupport {

    FindCommentListUseCase findCommentListUseCase = mock(FindCommentListUseCase.class);

    @Override
    protected Object initController() {
        return new FindCommentListController(findCommentListUseCase);
    }

    @Nested
    @DisplayName("[findCommentList] 리뷰를 조회하는 API")
    class Describe_findCommentList {

        @Test
        @DisplayName("[success] 조회된 리뷰가 있을 때 리뷰를 응답한다.")
        void success() throws Exception {
            // given
            Long productId = 1L;
            FindCommentListRequest request = FindCommentListRequest.builder()
                .page(0)
                .size(10)
                .build();
            when(findCommentListUseCase.findCommentList(any()))
                .thenReturn(List.of(
                    FindCommentListServiceResponse.builder()
                        .customerEmail("email1")
                        .comment("Great product!")
                        .build(),
                    FindCommentListServiceResponse.builder()
                        .customerEmail("email2")
                        .comment("Not bad.")
                        .build()
                ));

            // when then
            performDocument(request, productId, status().isOk(), "success", "find-comment-list",
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.ARRAY)
                    .description("리뷰 목록"),
                fieldWithPath("data[].customerEmail").type(JsonFieldType.STRING)
                    .description("구매자 이메일"),
                fieldWithPath("data[].comment").type(JsonFieldType.STRING)
                    .description("리뷰")
            );
        }

        @Test
        @DisplayName("[success] 조회된 리뷰가 없을 때 예외를 응답한다.")
        void error() throws Exception {
            // given
            Long productId = 2L;
            FindCommentListRequest request = FindCommentListRequest.builder()
                .page(0)
                .size(10)
                .build();
            when(findCommentListUseCase.findCommentList(any()))
                .thenThrow(new CustomNotFoundException(ErrorCode.DoesNotExist_COMMENT_INFO));

            // when then
            performErrorDocument(request, productId, status().isNotFound(), "조회된 리뷰 없음");
        }

        @Test
        @DisplayName("[success] 조회된 리뷰에 해당하는 상품 정보가 없을때 예외를 응답한다.")
        void error2() throws Exception {
            // given
            Long productId = 3L;
            FindCommentListRequest request = FindCommentListRequest.builder()
                .page(0)
                .size(10)
                .build();
            when(findCommentListUseCase.findCommentList(any()))
                .thenThrow(new CustomNotFoundException(ErrorCode.DoesNotExist_PROUCT_INFO));

            // when then
            performErrorDocument(request, productId, status().isNotFound(), "조회된 상품 정보 없음");
        }
    }

    private void performDocument(FindCommentListRequest request, Long productId,
        ResultMatcher status, String docIdentifier,
        String responseSchema, FieldDescriptor... responseFields)
        throws Exception {

        mockMvc.perform(get("/products/{productId}/comments", productId)
                .queryParam("page", String.valueOf(request.getPage()))
                .queryParam("size", String.valueOf(request.getSize())))
            .andDo(print())
            .andExpect(status)
            .andDo(MockMvcRestDocumentationWrapper.document("[find-comment-list] " + docIdentifier,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Comment")
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
                        .requestSchema(Schema.schema("[request] find-comment-list"))
                        .responseSchema(Schema.schema("[response] " + responseSchema))
                        .build())
                )
            );
    }

    private void performErrorDocument(FindCommentListRequest request, Long productId,
        ResultMatcher status, String identifier) throws Exception {

        performDocument(request, productId, status, identifier, "[response] error",
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
