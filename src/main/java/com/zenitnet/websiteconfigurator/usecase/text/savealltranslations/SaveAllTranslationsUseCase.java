package com.zenitnet.websiteconfigurator.usecase.text.savealltranslations;

import com.zenitnet.websiteconfigurator.domain.site.SiteService;
import com.zenitnet.websiteconfigurator.domain.text.TextEntity;
import com.zenitnet.websiteconfigurator.domain.text.TextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@CommonsLog
@Component
@RequiredArgsConstructor
public class SaveAllTranslationsUseCase {

    private final TextService textService;
    private final SiteService siteService;

    @Transactional
    public int execute(SaveAllTranslationsRequest request) {
        var site = siteService.findById(request.siteId());
        int savedCount = 0;

        for (var entry : request.translations().entrySet()) {
            String language = entry.getKey().toLowerCase();
            String contentValue = entry.getValue();

            var existing = textService.findByKeyAndSiteAndLanguage(request.translationKey(), site.getId(), language);

            if (existing.isPresent()) {
                var updated = existing.get().toBuilder()
                    .contentValue(contentValue)
                    .build();
                textService.save(updated);
            } else {
                var entity = TextEntity.builder()
                    .translationKey(request.translationKey())
                    .contentValue(contentValue)
                    .language(language)
                    .site(site)
                    .build();
                textService.save(entity);
            }

            savedCount++;
        }

        return savedCount;
    }
}
