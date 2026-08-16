package com.subtrack.ai.dto.response;

import java.util.List;

public record TranslateRequest(
        String targetLang,
        List<String> lines
) {
}
