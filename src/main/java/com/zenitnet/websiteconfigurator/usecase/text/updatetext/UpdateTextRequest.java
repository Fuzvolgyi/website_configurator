package com.zenitnet.websiteconfigurator.usecase.text.updatetext;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateTextRequest(
    @NotBlank @Size(max = 255) @Pattern(regexp = "^[a-z0-9._]+$") String translationKey,
    @NotBlank @Size(max = 5000) String contentValue
) {}
