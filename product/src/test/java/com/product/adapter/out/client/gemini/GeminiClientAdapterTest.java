package com.product.adapter.out.client.gemini;

import com.product.IntegrationTestSupport;
import com.product.adapter.out.client.gemini.GeminiEmbeddingResponse.GeminiEmbedding;
import com.product.adapter.out.client.gemini.GeminiQueryResponse.GeminiQueryCandidate;
import com.product.adapter.out.client.gemini.GeminiQueryResponse.GeminiQueryCandidate.GeminiQueryCandidateContent;
import com.product.adapter.out.client.gemini.GeminiQueryResponse.GeminiQueryCandidate.GeminiQueryCandidateContent.GeminiQueryCandidateContentPart;
import com.product.infrastructure.util.JsonUtil;
import java.io.IOException;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

class GeminiClientAdapterTest extends IntegrationTestSupport {

    @Autowired
    private GeminiClientAdapter geminiClientAdapter;
    static MockWebServer geminiServer;

    @BeforeAll
    static void setup() throws IOException {
        geminiServer = new MockWebServer();
        geminiServer.start(3521);
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) throws IOException {
        registry.add("service-constant.external.gemini.host",
            () -> geminiServer.url("/").toString());
        registry.add("service-constant.external.gemini.token", () -> "fake-token");
    }

    @AfterAll
    static void shutdown() throws IOException {
        geminiServer.shutdown();
    }

    @Nested
    @DisplayName("[embedding] 문자열을 임베딩하는 메소드")
    class Describe_embedding {

        @Test
        @DisplayName("[success] gemini API 를 통해 문자열을 임베딩한다.")
        void success() {
            // given
            String document = "test";
            String responseBody = JsonUtil.toJsonString(GeminiEmbeddingResponse.builder()
                .embedding(GeminiEmbedding.builder()
                    .values(new float[]{123f, 456f})
                    .build())
                .build());
            geminiServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-type", "application/json")
                .setBody(responseBody));

            // when
            float[] result = geminiClientAdapter.embedding(document);

            // then
            assert result.length == 2;
            assert result[0] == 123;
            assert result[1] == 456;
        }

        @Test
        @DisplayName("[error] API 호출에 실패하면 fallback 메서드가 실행된다.")
        void error(CapturedOutput output) {
            // given
            String document = "test";
            geminiServer.enqueue(new MockResponse()
                .setResponseCode(500));

            // when
            float[] result = geminiClientAdapter.embedding(document);

            // then
            assert result == null;
            assert output.toString().contains("[gemini] embeddingFallback :");
        }
    }

    @Nested
    @DisplayName("[query] gemini 검색 결과를 응답하는 메소드")
    class Describe_query {

        @Test
        @DisplayName("[success] gemini API 를 검색 결과를 응답한다.")
        void success() {
            // given
            String query = "hello world";
            String responseBody = JsonUtil.toJsonString(GeminiQueryResponse.builder()
                .candidates(List.of(GeminiQueryCandidate.builder()
                    .content(GeminiQueryCandidateContent.builder()
                        .parts(List.of(GeminiQueryCandidateContentPart.builder()
                            .text("만나서 반갑습니다.")
                            .build()))
                        .build())
                    .build()))
                .build());
            geminiServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-type", "application/json")
                .setBody(responseBody));

            // when
            String result = geminiClientAdapter.query(query);

            // then
            assert result.equals("만나서 반갑습니다.");
        }

        @Test
        @DisplayName("[error] API 호출에 실패하면 fallback 메서드가 실행된다.")
        void error(CapturedOutput output) {
            // given
            String query = "test";
            geminiServer.enqueue(new MockResponse()
                .setResponseCode(500));

            // when
            String result = geminiClientAdapter.query(query);

            // then
            assert result == null;
            assert output.toString().contains("[gemini] queryFallback :");
        }
    }
}