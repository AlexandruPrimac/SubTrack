package com.subtrack.service;

import com.subtrack.ai.dto.request.TranslationResponse;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface SongTranslatedAiService {

    @SystemMessage(fromResource ="prompts/translatePrompt.txt" )
    TranslationResponse translateLines(
            @V("targetLang") String targetLang,
            @UserMessage String linesJsonArray
    );

}