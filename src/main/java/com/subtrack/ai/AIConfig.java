package com.subtrack.ai;

import com.subtrack.config.AiConfigurationProperties;
import com.subtrack.config.ModelProvider;
import com.subtrack.service.SongTranslatedAiService;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.mistralai.MistralAiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AIConfig {

    private final AiConfigurationProperties aiProps;
    private final Environment environment;

    @Bean("mainChatModel")
    @ConditionalOnProperty(name = "ai.main-provider.provider", havingValue = "mistral")
    public ChatModel mistralMain() {
        ModelProvider conf = aiProps.getMainProvider();

        return MistralAiChatModel.builder()
                .apiKey(conf.getApiKey())
                .modelName(conf.getModelName())
                .maxRetries(aiProps.getMaxRetries())
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public SongTranslatedAiService songTranslatedAiService(ChatModel mainChatModel) {
        return AiServices.builder(SongTranslatedAiService.class)
                .chatModel(mainChatModel)
                .build();
    }

}
