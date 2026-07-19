package com.zenitnet.websiteconfigurator.usecase.picture.createpicture;

import lombok.Builder;

@Builder
public record CreatePictureResponse(
    Long id,
    String storageUrl,
    String altText,
    int displayOrder,
    Long siteId
) {}
