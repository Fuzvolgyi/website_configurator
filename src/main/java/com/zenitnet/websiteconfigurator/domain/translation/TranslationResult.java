package com.zenitnet.websiteconfigurator.domain.translation;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TranslationResult {

    private final boolean successful;
    private final String translatedText;
    private final String errorMessage;

    public static TranslationResult success(String translatedText) {
        return TranslationResult.builder()
            .successful(true)
            .translatedText(translatedText)
            .build();
    }

    public static TranslationResult failure(String errorMessage) {
        return TranslationResult.builder()
            .successful(false)
            .errorMessage(errorMessage)
            .build();
    }
}
