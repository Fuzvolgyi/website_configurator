package com.zenitnet.websiteconfigurator.usecase.text.savewithtranslation;

import lombok.Builder;

import java.util.List;

@Builder
public record SaveTextWithTranslationResponse(
    String translationKey,
    int savedLanguages,
    List<TranslationError> errors
) {

    @Builder
    public record TranslationError(
        String language,
        String message
    ) {}
}
