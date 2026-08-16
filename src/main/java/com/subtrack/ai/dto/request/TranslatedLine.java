package com.subtrack.ai.dto.request;

public record TranslatedLine(
        String originalText,
        String literalText,
        String idiomaticText
) {}
