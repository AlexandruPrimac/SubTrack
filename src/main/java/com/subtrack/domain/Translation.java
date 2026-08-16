package com.subtrack.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
        name = "translations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"original_text", "target_lang"})
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Translation {

    @Id
    @GeneratedValue
    private UUID id;

    // Deliberately keyed by the original line's text + target language, NOT
    // by a specific LyricLine id. A chorus repeated 3x in a song shares one
    // row here instead of paying for an LLM call (and storage) 3 times.
    // The service layer joins LyricLine.originalText -> this table at read time.
    @Column(name = "original_text", columnDefinition = "TEXT", nullable = false)
    private String originalText;

    @Column(name = "target_lang", nullable = false)
    private String targetLang;

    @Column(columnDefinition = "TEXT")
    private String literalText;

    @Column(columnDefinition = "TEXT")
    private String idiomaticText;

    private String model;

    @Builder.Default
    private Instant createdAt = Instant.now();
}