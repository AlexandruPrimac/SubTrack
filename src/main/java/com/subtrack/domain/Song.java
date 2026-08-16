package com.subtrack.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "songs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Song {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String artist;

    @Column
    private String album;

    private Integer durationMs;

    private String sourceLang;

    /// Plays of the same track hit cache instead of re-matching against LRCLIB.
    @Column(unique = true)
    private String spotifyTrackId;

    private Long lrclibId;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("lineIndex ASC")
    @Builder.Default
    private List<LyricLine> lyricLines = new ArrayList<>();


}
