package com.product.adapter.out.client.gemini;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class GeminiQueryRequestTest {

    @Nested
    @DisplayName("[of] 입력값으로 GeminiQueryRequest 를 생성하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 입력값으로 GeminiQueryRequest 를 잘 생성하는지 확인한다.")
        void success() {
            // given
            String content = "hello";

            // when
            GeminiQueryRequest result = GeminiQueryRequest.of(content);

            // then
            assert result.contents().parts().getFirst().text().equals(content);
        }
    }
}