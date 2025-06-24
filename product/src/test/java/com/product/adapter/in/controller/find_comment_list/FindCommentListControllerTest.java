package com.product.adapter.in.controller.find_comment_list;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.product.ControllerTestSupport;
import com.product.application.service.find_comment_list.FindCommentListServiceResponse;
import com.product.application.service.find_comment_list.FindCommentListServiceResponse.FindCommentListServiceResponseItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class FindCommentListControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[findCommentList] 리뷰를 조회하는 API")
    class Describe_findCommentList {

        @Test
        @DisplayName("[success] API 를 요청 했을 때 200코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            Long productId = 1L;
            when(findCommentListUseCase.findCommentList(any()))
                .thenReturn(FindCommentListServiceResponse.builder()
                    .page(0)
                    .size(10)
                    .commentCount(1)
                    .comments(List.of(FindCommentListServiceResponseItem.builder()
                        .comment("좋아요")
                        .customerEmail("test")
                        .build()))
                    .build());

            // when
            ResultActions actions = mockMvc.perform(
                get("/products/{productId}/comments", productId)
            );

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"));
        }
    }
}