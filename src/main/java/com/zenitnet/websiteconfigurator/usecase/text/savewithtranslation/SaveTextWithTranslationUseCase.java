package com.zenitnet.websiteconfigurator.usecase.text.savewithtranslation;

import com.zenitnet.websiteconfigurator.domain.site.SiteService;
import com.zenitnet.websiteconfigurator.domain.text.TextEntity;
import com.zenitnet.websiteconfigurator.domain.text.TextService;
import com.zenitnet.websiteconfigurator.domain.translation.TranslationResult;
import com.zenitnet.websiteconfigurator.domain.translation.TranslationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@CommonsLog
@Component
@RequiredArgsConstructor
public class SaveTextWithTranslationUseCase {

    private final TextService textService;
    private final SiteService siteService;
    private final TranslationService translationService;

    @Transactional
    public SaveTextWithTranslationResponse execute(SaveTextWithTranslationRequest request) {
        var site = siteService.findById(request.siteId());
        var sourceLanguage = request.sourceLanguage().toLowerCase();
        var targetLanguages = translationService.getTargetLanguages();
        var errors = new ArrayList<SaveTextWithTranslationResponse.TranslationError>();
        var translatedTexts = new java.util.HashMap<String, String>();

        translatedTexts.put(sourceLanguage, request.contentValue());

        for (String targetLang : targetLanguages) {
            String normalizedTarget = targetLang.trim().toLowerCase();
            if (normalizedTarget.equals(sourceLanguage)) {
                continue;
            }

            TranslationResult result = translationService.translate(request.contentValue(), targetLang.trim(), request.sourceLanguage());

            if (result.isSuccessful()) {
                translatedTexts.put(normalizedTarget, result.getTranslatedText());
            } else {
                errors.add(SaveTextWithTranslationResponse.TranslationError.builder()
                    .language(normalizedTarget)
                    .message(result.getErrorMessage())
                    .build());
            }
        }

        if (!errors.isEmpty()) {
            return SaveTextWithTranslationResponse.builder()
                .translationKey(request.translationKey())
                .savedLanguages(0)
                .errors(errors)
                .build();
        }

        int savedCount = 0;
        for (var entry : translatedTexts.entrySet()) {
            saveText(request.translationKey(), entry.getValue(), entry.getKey(), site.getId());
            savedCount++;
        }

        return SaveTextWithTranslationResponse.builder()
            .translationKey(request.translationKey())
            .savedLanguages(savedCount)
            .errors(errors)
            .build();
    }

    private int saveText(String translationKey, String contentValue, String language, Long siteId) {
        var existing = textService.findByKeyAndSiteAndLanguage(translationKey, siteId, language);

        if (existing.isPresent()) {
            var updated = existing.get().toBuilder()
                .contentValue(contentValue)
                .build();
            textService.save(updated);
        } else {
            var site = siteService.findById(siteId);
            var entity = TextEntity.builder()
                .translationKey(translationKey)
                .contentValue(contentValue)
                .language(language)
                .site(site)
                .build();
            textService.save(entity);
        }

        return 1;
    }
}
