package com.zenitnet.websiteconfigurator.usecase.text.gettexts;

import com.zenitnet.websiteconfigurator.domain.text.TextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;

import java.util.List;

@CommonsLog
@Component
@RequiredArgsConstructor
public class GetTextsBySiteUseCase {

    private final TextService textService;

    public List<GetTextsBySiteResponse> execute(Long siteId) {
        return textService.findBySiteId(siteId).stream()
            .map(entity -> GetTextsBySiteResponse.builder()
                .id(entity.getId())
                .translationKey(entity.getTranslationKey())
                .contentValue(entity.getContentValue())
                .siteId(entity.getSite().getId())
                .build())
            .toList();
    }
}
