package com.subtrack.ai.dto.request;

import java.util.List;

public record TranslationResponse(
        List<TranslatedLine> lines
) {
}
