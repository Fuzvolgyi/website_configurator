package com.zenitnet.websiteconfigurator.usecase.text.savewithtranslation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SaveTextWithTranslationRequest(
    @NotBlank @Size(max = 255) String translationKey,
    @NotBlank @Size(max = 5000) String contentValue,
    @NotNull Long siteId,
    @NotBlank String sourceLanguage
) {}
