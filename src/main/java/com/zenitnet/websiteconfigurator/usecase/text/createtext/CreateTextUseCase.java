package com.zenitnet.websiteconfigurator.usecase.text.createtext;

import com.zenitnet.websiteconfigurator.domain.site.SiteService;
import com.zenitnet.websiteconfigurator.domain.text.TextEntity;
import com.zenitnet.websiteconfigurator.domain.text.TextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

@CommonsLog
@Component
@RequiredArgsConstructor
public class CreateTextUseCase {

    private final TextService textService;
    private final SiteService siteService;

    public CreateTextResponse execute(CreateTextRequest request) {
        textService.validateUniqueTranslationKey(request.translationKey(), request.siteId(), null);
        var site = siteService.findById(request.siteId());

        var entity = TextEntity.builder()
            .translationKey(request.translationKey())
            .contentValue(request.contentValue())
            .site(site)
            .build();

        var saved = textService.save(entity);

        return CreateTextResponse.builder()
            .id(saved.getId())
            .translationKey(saved.getTranslationKey())
            .contentValue(saved.getContentValue())
            .siteId(saved.getSite().getId())
            .build();
    }
}
