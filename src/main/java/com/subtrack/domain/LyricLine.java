package com.subtrack.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "lyric_lines", indexes = @Index(name = "idx_lyric_lines_song", columnList = "song_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LyricLine {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    private int lineIndex;

    // Nullable — LRCLIB sometimes only has plain, unsynced lyrics for a track.
    // Null means "no highlight for this line, just show it statically.
    private Integer timestampMs;

    @Column(columnDefinition = "TEXT")
    private String originalText;
}
