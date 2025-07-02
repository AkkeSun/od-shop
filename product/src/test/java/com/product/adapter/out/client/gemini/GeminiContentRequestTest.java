package com.product.adapter.out.client.gemini;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class GeminiContentRequestTest {

    @Nested
    @DisplayName("[of] 입력값으로 GeminiContentRequest 를 생성하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 입력값으로 GeminiContentRequest 를 잘 생성하는지 확인한다.")
        void success() {
            // given
            String text = "hello";

            // when
            GeminiContentRequest result = GeminiContentRequest.of(text);

            // then
            assert result.parts().getFirst().text().equals(text);
        }
    }
}