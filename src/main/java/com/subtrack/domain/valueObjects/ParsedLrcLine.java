package com.subtrack.domain.valueObjects;

public record ParsedLrcLine(
        Integer timestampMs,
        String text
) {
}
