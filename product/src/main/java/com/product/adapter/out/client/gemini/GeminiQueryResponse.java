package com.product.adapter.out.client.gemini;

import java.util.List;
import lombok.Builder;

@Builder
public record GeminiQueryResponse(
    List<GeminiQueryCandidate> candidates,
    GeminiUsageMetadata usageMetadata,
    String modelVersion,
    String responseId
) {

    record GeminiQueryCandidate(
        GeminiQueryCandidateContent content,
        String finishReason,
        double avgLogprobs
    ) {

        record GeminiQueryCandidateContent(
            List<GeminiQueryCandidateContentPart> parts,
            String role
        ) {

            @Builder
            record GeminiQueryCandidateContentPart(
                String text
            ) {

            }
        }
    }

    record GeminiUsageMetadata(
        int promptTokenCount,
        int candidatesTokenCount,
        int totalTokenCount,
        List<TokenDetail> promptTokensDetails,
        List<TokenDetail> candidatesTokensDetails
    ) {

        record TokenDetail(
            String modality,
            int tokenCount
        ) {

        }
    }

    String getResponse() {
        return candidates.getFirst().content.parts.getFirst().text;
    }
}
