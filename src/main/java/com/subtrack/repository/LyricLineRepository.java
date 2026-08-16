package com.subtrack.repository;

import com.subtrack.domain.LyricLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LyricLineRepository extends JpaRepository<LyricLine, UUID> {

    List<LyricLine> findBySongIdOrderByLineIndexAsc(UUID songId);
}
