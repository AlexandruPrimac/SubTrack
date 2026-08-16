package com.subtrack.repository;

import com.subtrack.domain.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TranslationRepository extends JpaRepository<Translation, UUID> {

    // Plain derived query is fine here — target_lang is a single fixed value
    // per call, so this is just an IN plus an equals, not a real tuple query.
    // Used to check which of a song's lines are already cached before
    // deciding which ones actually need an LLM call.
    List<Translation> findByTargetLangAndOriginalTextIn(String targetLang, Collection<String> originalTexts);

    Optional<Translation> findByOriginalTextAndTargetLang(String originalText, String targetLang);

    // Native SQL earns its place here: ON CONFLICT DO NOTHING has no
    // JPQL/Hibernate equivalent, and it's what makes two concurrent requests
    // translating the same line (e.g. a repeated chorus, or two users hitting
    // the same song at once) safe instead of racing into a duplicate-key
    // exception. Caller must re-fetch afterward via
    // findByOriginalTextAndTargetLang to get whichever row "won."
    @Modifying
    @Query(value = """
        INSERT INTO translations (id, original_text, target_lang, literal_text, idiomatic_text, model, created_at)
        VALUES (:id, :originalText, :targetLang, :literalText, :idiomaticText, :model, now())
        ON CONFLICT (original_text, target_lang) DO NOTHING
        """, nativeQuery = true)
    void upsertIgnoreConflict(
            @Param("id") UUID id,
            @Param("originalText") String originalText,
            @Param("targetLang") String targetLang,
            @Param("literalText") String literalText,
            @Param("idiomaticText") String idiomaticText,
            @Param("model") String model
    );
}
