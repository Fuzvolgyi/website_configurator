package com.zenitnet.websiteconfigurator.usecase.picture.updatepicture;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdatePictureRequest(
    @NotBlank String storageUrl,
    @NotBlank @Size(max = 255) String altText,
    @Min(1) @Max(999) int displayOrder
) {}
