package com.zenitnet.websiteconfigurator.usecase.text.savealltranslations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.Map;

@Builder
public record SaveAllTranslationsRequest(
    @NotBlank @Size(max = 255) String translationKey,
    @NotNull Long siteId,
    @NotEmpty Map<String, String> translations
) {}
