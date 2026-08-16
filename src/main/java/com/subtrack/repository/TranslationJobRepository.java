package com.subtrack.repository;

import com.subtrack.domain.TranslationJob;
import com.subtrack.domain.enums.TranslationJobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface TranslationJobRepository extends JpaRepository<TranslationJob, UUID> {

    // Prevents kicking off a duplicate job if the same song+language is
    // requested again while one is already PENDING or IN_PROGRESS.
    Optional<TranslationJob> findFirstBySongIdAndTargetLangAndStatusIn(UUID songId, String targetLang, Collection<TranslationJobStatus> statuses);
}
