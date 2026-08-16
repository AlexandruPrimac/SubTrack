package com.subtrack.controller.dto;

public record SyncedLyricLineDto(
        int lineIndex,
        Integer timestampMs,
        String originalText,
        String literalText,
        String idiomaticText
) {
}
