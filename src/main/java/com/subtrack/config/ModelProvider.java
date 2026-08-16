package com.subtrack.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModelProvider {

    @NotBlank
    private String provider;

    @NotBlank
    private String ModelName;
    
    private String apiKey;
    private String baseUrl;
}
