package com.zenitnet.websiteconfigurator.usecase.picture.createpicture;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreatePictureRequest(
    @NotBlank String storageUrl,
    @NotBlank @Size(max = 255) String altText,
    @Min(1) @Max(999) int displayOrder,
    @NotNull Long siteId
) {}
