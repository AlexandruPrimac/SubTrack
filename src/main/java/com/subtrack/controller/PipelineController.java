package com.subtrack.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.subtrack.ai.dto.request.TranslatedLine;
import com.subtrack.ai.dto.request.TranslationResponse;
import com.subtrack.controller.dto.SyncedLyricLineDto;
import com.subtrack.domain.valueObjects.ParsedLrcLine;
import com.subtrack.service.LrcLibService;
import com.subtrack.service.SongTranslatedAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class PipelineController {

    private final LrcLibService lrcLibService;
    private final SongTranslatedAiService aiService;
    private final ObjectMapper objectMapper;

    @PostMapping("/fetch-and-translate")
    public List<SyncedLyricLineDto> fetchAndTranslate(@RequestBody FetchAndTranslateRequest request) throws JsonProcessingException {
        // 1. Fetch timestamped lines from LRCLIB
        List<ParsedLrcLine> parsedLines = lrcLibService.fetchParsedLyricLines(
                request.trackName(),
                request.artistName(),
                request.albumName()
        );

        // 2. Extract plain text array to send to Mistral
        List<String> rawTexts = parsedLines.stream()
                .map(ParsedLrcLine::text)
                .toList();

        // 3. Request translation from LLM
        String targetLang = request.targetLanguage() != null ? request.targetLanguage() : "English";
        TranslationResponse aiResponse = aiService.translateLines(targetLang, objectMapper.writeValueAsString(rawTexts));

        // 4. Zip LRCLIB timestamps + original text with LLM translations
        List<TranslatedLine> translatedLines = aiResponse.lines();
        List<SyncedLyricLineDto> syncedOutput = new ArrayList<>();

        for (int i = 0; i < parsedLines.size(); i++) {
            ParsedLrcLine lrc = parsedLines.get(i);

            // Fallback safety if LLM response size mismatches
            String literal = (translatedLines != null && i < translatedLines.size())
                    ? translatedLines.get(i).literalText()
                    : lrc.text();
            String idiomatic = (translatedLines != null && i < translatedLines.size())
                    ? translatedLines.get(i).idiomaticText()
                    : lrc.text();

            syncedOutput.add(new SyncedLyricLineDto(
                    i,
                    lrc.timestampMs(),
                    lrc.text(),
                    literal,
                    idiomatic
            ));
        }

        return syncedOutput;
    }

    public record FetchAndTranslateRequest(
            String trackName,
            String artistName,
            String albumName,
            String targetLanguage
    ) {}
}
