package com.subtrack.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "ai")
@Validated
@Getter
@Setter
public class AiConfigurationProperties {

    @Valid
    private final ModelProvider mainProvider = new ModelProvider();

    @Valid
    private final ModelProvider backupProvider = new ModelProvider();

    @Min(0)
    private int maxOutputTokens = 8000;
    @Min(0)
    private int maxRetries = 0;
}
