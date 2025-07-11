package com.product.adapter.in.controller.find_review_list;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.product.ControllerTestSupport;
import com.product.application.service.find_review_list.FindReviewListServiceResponse;
import com.product.application.service.find_review_list.FindReviewListServiceResponse.FindReviewListServiceResponseItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class FindReviewListControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[findReviewList] 리뷰를 조회하는 API")
    class Describe_findReviewList {

        @Test
        @DisplayName("[success] API 를 요청 했을 때 200코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            Long productId = 1L;
            when(findReviewListUseCase.findReviewList(any()))
                .thenReturn(FindReviewListServiceResponse.builder()
                    .page(0)
                    .size(10)
                    .reviewCount(1)
                    .reviews(List.of(FindReviewListServiceResponseItem.builder()
                        .review("좋아요")
                        .customerEmail("test")
                        .build()))
                    .build());

            // when
            ResultActions actions = mockMvc.perform(
                get("/products/{productId}/reviews", productId)
            );

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"));
        }
    }
}