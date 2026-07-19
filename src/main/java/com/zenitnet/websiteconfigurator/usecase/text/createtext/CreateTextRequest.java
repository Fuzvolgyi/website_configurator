package com.zenitnet.websiteconfigurator.usecase.text.createtext;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateTextRequest(
    @NotBlank @Size(max = 255) @Pattern(regexp = "^[a-z0-9._]+$") String translationKey,
    @NotBlank @Size(max = 5000) String contentValue,
    @NotNull Long siteId
) {}
