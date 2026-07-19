package com.zenitnet.websiteconfigurator.domain.text;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface TextRepository extends JpaRepository<TextEntity, Long> {

    List<TextEntity> findBySiteId(Long siteId);

    List<TextEntity> findBySiteIdAndLanguage(Long siteId, String language);

    List<TextEntity> findByLanguage(String language);

    Optional<TextEntity> findByTranslationKeyAndSiteId(String translationKey, Long siteId);

    Optional<TextEntity> findByTranslationKeyAndSiteIdAndLanguage(String translationKey, Long siteId, String language);
}
