package com.subtrack.service;

import com.subtrack.controller.dto.LrcLibResponse;
import com.subtrack.domain.valueObjects.ParsedLrcLine;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class LrcLibService {

    // Group 1: Minutes | Group 2: Seconds | Group 3: Millis/Centis | Group 4: Text
    private static final Pattern LRC_PATTERN = Pattern.compile("^\\[(\\d{1,3}):(\\d{2})[\\.:](\\d{2,3})\\]\\s*(.*)$");

    private final RestClient restClient = RestClient.builder()
            .baseUrl("https://lrclib.net")
            .defaultHeader("User-Agent", "SubTrackApp/1.0 (https://github.com/subtrack/subtrack)")
            .build();

    public List<ParsedLrcLine> fetchParsedLyricLines(String trackName, String artistName, String albumName) {
        LrcLibResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/get")
                        .queryParam("track_name", trackName)
                        .queryParam("artist_name", artistName)
                        .queryParamIfPresent("album_name", java.util.Optional.ofNullable(albumName))
                        .build())
                .retrieve()
                .body(LrcLibResponse.class);

        if (response == null) {
            throw new RuntimeException("No lyrics found on LRCLIB for track: " + trackName);
        }

        return extractLrcLines(response.syncedLyrics(), response.plainLyrics());
    }

    private List<ParsedLrcLine> extractLrcLines(String syncedLyrics, String plainLyrics) {
        List<ParsedLrcLine> lines = new ArrayList<>();

        if (syncedLyrics != null && !syncedLyrics.isBlank()) {
            for (String rawLine : syncedLyrics.split("\r?\n")) {
                String trimmed = rawLine.trim();
                Matcher matcher = LRC_PATTERN.matcher(trimmed);

                if (matcher.matches()) {
                    // Correct Group Indexing:
                    int minutes = Integer.parseInt(matcher.group(1)); // Group 1 -> Minutes
                    int seconds = Integer.parseInt(matcher.group(2)); // Group 2 -> Seconds
                    String millisStr = matcher.group(3);              // Group 3 -> Millis
                    int millis = Integer.parseInt(millisStr) * (millisStr.length() == 2 ? 10 : 1);

                    int timestampMs = (minutes * 60 * 1000) + (seconds * 1000) + millis;
                    String text = matcher.group(4).trim();           // Group 4 -> Lyric Text (String)

                    if (!text.isEmpty()) {
                        lines.add(new ParsedLrcLine(timestampMs, text));
                    }
                }
            }
        } else if (plainLyrics != null && !plainLyrics.isBlank()) {
            for (String line : plainLyrics.split("\r?\n")) {
                String text = line.trim();
                if (!text.isEmpty()) {
                    lines.add(new ParsedLrcLine(null, text));
                }
            }
        }

        return lines;
    }
}
