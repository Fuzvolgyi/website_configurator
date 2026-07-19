package com.zenitnet.websiteconfigurator.domain.text;

import com.zenitnet.websiteconfigurator.common.exception.BusinessLogicException;
import com.zenitnet.websiteconfigurator.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@CommonsLog
@Service
@RequiredArgsConstructor
public class TextService {

    private final TextRepository textRepository;

    public List<TextEntity> findBySiteId(Long siteId) {
        return textRepository.findBySiteId(siteId);
    }

    public List<TextEntity> findBySiteIdAndLanguage(Long siteId, String language) {
        return textRepository.findBySiteIdAndLanguage(siteId, language);
    }

    public List<TextEntity> findByLanguage(String language) {
        return textRepository.findByLanguage(language);
    }

    public List<TextEntity> findAll() {
        return textRepository.findAll();
    }

    public TextEntity findById(Long id) {
        return textRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Text not found with id: " + id));
    }

    public void validateUniqueTranslationKey(String translationKey, Long siteId, Long excludeId) {
        Optional<TextEntity> existing = textRepository.findByTranslationKeyAndSiteId(translationKey, siteId);
        if (existing.isPresent() && !existing.get().getId().equals(excludeId)) {
            throw new BusinessLogicException(
                "Translation key '" + translationKey + "' already exists for this site");
        }
    }

    public Optional<TextEntity> findByKeyAndSiteAndLanguage(String translationKey, Long siteId, String language) {
        return textRepository.findByTranslationKeyAndSiteIdAndLanguage(translationKey, siteId, language);
    }

    public TextEntity save(TextEntity entity) {
        return textRepository.save(entity);
    }

    public void delete(TextEntity entity) {
        textRepository.delete(entity);
    }
}
