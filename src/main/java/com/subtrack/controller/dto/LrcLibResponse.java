package com.subtrack.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LrcLibResponse(
        Long id,
        @JsonProperty("trackName") String trackName,
        @JsonProperty("artistName") String artistName,
        @JsonProperty("albumName") String albumName,
        Integer duration,
        Boolean instrumental,
        String plainLyrics,
        String syncedLyrics
) {
}
