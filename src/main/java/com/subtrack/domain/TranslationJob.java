package com.subtrack.domain;

import com.subtrack.domain.enums.TranslationJobStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "translation_jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranslationJob {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @Column(name = "target_lang", nullable = false)
    private String targetLang;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TranslationJobStatus status = TranslationJobStatus.PENDING;

    @Builder.Default
    private Instant requestedAt = Instant.now();

    private Instant completedAt;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;
}
