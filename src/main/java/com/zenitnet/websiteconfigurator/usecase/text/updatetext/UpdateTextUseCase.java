package com.zenitnet.websiteconfigurator.usecase.text.updatetext;

import com.zenitnet.websiteconfigurator.domain.text.TextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

@CommonsLog
@Component
@RequiredArgsConstructor
public class UpdateTextUseCase {

    private final TextService textService;

    public UpdateTextResponse execute(Long id, UpdateTextRequest request) {
        var entity = textService.findById(id);
        textService.validateUniqueTranslationKey(request.translationKey(), entity.getSite().getId(), id);

        var updated = entity.toBuilder()
            .translationKey(request.translationKey())
            .contentValue(request.contentValue())
            .build();

        var saved = textService.save(updated);

        return UpdateTextResponse.builder()
            .id(saved.getId())
            .translationKey(saved.getTranslationKey())
            .contentValue(saved.getContentValue())
            .siteId(saved.getSite().getId())
            .build();
    }
}
