package com.subtrack.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.subtrack.ai.dto.request.TranslationResponse;
import com.subtrack.ai.dto.response.TranslateRequest;
import com.subtrack.service.SongTranslatedAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    /// Service
    private final SongTranslatedAiService songTranslatedAiService;
    private final ObjectMapper objectMapper;

    @PostMapping("/translate-test")
    public TranslationResponse translateTest(@RequestBody TranslateRequest request) throws JsonProcessingException {
        String linesJson = objectMapper.writeValueAsString(request.lines());
        return songTranslatedAiService.translateLines(request.targetLang(), linesJson);
    }

}
