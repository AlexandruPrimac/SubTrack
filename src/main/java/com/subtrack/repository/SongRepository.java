package com.subtrack.repository;

import com.subtrack.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SongRepository extends JpaRepository<Song, UUID> {

    // The fast path: once a Spotify track has been resolved once, every
    // future play of it hits this instead of re-matching against LRCLIB.
    Optional<Song> findBySpotifyTrackId(String spotifyTrackId);

    // Fallback for a track we haven't cached a Spotify id for yet. Duration
    // tolerance (pass e.g. durationMs ± 2000) covers the fact that LRCLIB's
    // indexed master and the track actually playing can differ by a second
    // or two even when they're the same recording.
    List<Song> findByArtistIgnoreCaseAndTitleIgnoreCaseAndDurationMsBetween(String artist, String title, int minDurationMs, int maxDurationMs);
}
